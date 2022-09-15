'use strict'

const studyDetail_left = document.querySelector(".studyDetail-left");
const studyDetail_top = document.querySelector(".studyDetail-top");
const studyDetail_center = document.querySelector(".studyDetail-center");
const studyDetail_right = document.querySelector(".studyDetail-right");
const studyId = document.querySelector(".studyId").value;
const userNickname = document.querySelector(".userNickname").value;
let teamMateList;

window.onload = async () => {
  teamMateList = await findTeamMateList(studyId, userNickname);
  checkTeamMate(teamMateList);
  findStudyDetail(studyId);
}

let isParticipation = false;
let isTeamMate = false;
let isDelete = false;
let cnt = 0;

function findStudyDetail(studyId) {
  console.log("findStudyDetail 실행");
  fetch(`/study/detail/${studyId}/study-data`)
  .then(response => response.json())
  .then(data => {
    displayStudy(data, teamMateList);
  });
}

function displayStudy(data, teamMateList) {
  console.log("displayStudy 실행");
  // StudyDetail-left [start]
  let html = '';
  html += `
    <div>
      <p id = "studyTag"># ${data.studyTag}</p>
      <a href="/study/coverUpload/${studyId}">
        <img class="studyCover flex-shrink-0" src="${data.studyCoverUrl}">
      </a>
  `;

  console.log("isParticipation : " + isParticipation);
  // 팀원이 아니며 아직 모집 중일 때,
  if (!isParticipation && data.studyStatus == 'RECRUIT') {
    html += `
      <button onclick = "create()" id = "participationbtn" class="btn btn-outline-primary mt-3">참가 신청</button>
    `;
  }

  // 팀원이지만 팀장이 아니며, 아직 진행 중이 아닌 경우,
  if(isTeamMate && (userNickname != data.leader) && (data.studyStatus == 'RECRUIT' || data.studyStatus == 'RECRUIT_COMPLETE')){
    html += `
      <button onclick = "quit()" id="quitbtn" class="btn btn-outline-secondary mt-3">탈퇴하기</button>
    `;
  }

  html += `</div>`
  studyDetail_left.innerHTML = html;
  // StudyDetail-left [end]

  // StudyDetail-top [start]
  html = '';
  html += `
    <div id = "studyTitle">${data.studyTitle}</div>`;

  if ((data.studyStatus == 'RECRUIT' || data.studyStatus == 'RECRUIT_COMPLETE')
      && userNickname == data.leader) {
    html += `<a id = "studyUpdate" href="/study/update/${studyId}">수정</a>`
  }

  html += `
    <div class = "status">
      <div class = "people">
          <i id="people-icon" class="bi bi-people-fill"></i>
          <span>인원 : ${data.acceptedStudyMember} / ${data.studyPeople}명</span>
      </div>
   `;

  if (data.studyStatus == 'RECRUIT') {
    html += `<p id = "recruit">모집 중</p>`;
  }else if (data.studyStatus == 'RECRUIT_COMPLETE') {
    html += `<p id = "recruit">모집 완료</p>`;
  }else if (data.studyStatus == 'PROGRESS') {
    html += `<p id = "progress">진행 중</p>`;
  } else {
    html += `<p id = "complete">완료</p>`;
  }
  html += `</div>`;

  studyDetail_top.innerHTML = html;
  // StudyDetail-top [end]

  // studyDetail-center[start]
  html = '';
  if (data.studyType == 'BOOK') {
    html += `
      <div id = "typeIcon">
        <i class="bi bi-book-half" style="font-size: 1.5rem;"></i>
        <span>상세 정보</span>
      </div>
      <p>책 이름 : ${data.bookName}</p>
      <p>작가 : ${data.bookAuthor}</p>
      <p>출판사 : ${data.bookPublisher}</p>
      <p>책 쪽수 : ${data.bookPage} 페이지</p>
      <p>ISBN : ${data.bookIsbn}</p>
    `;
  } else {
    html += `
      <div id = "typeIcon">
        <i class="bi bi-play-btn" style="font-size: 1.5rem;"></i>
        <span>상세 정보</span>
      </div>
      <p>강의 이름 : ${data.lectureName}</p>
      <p>강사진 : ${data.lecturer}</p>
      <p>강의 개수 : ${data.lectureNumber} 강</p>
    `;
  }

  studyDetail_center.innerHTML = html;
  // studyDetail-center[end]

  // studyDetail-right[start]
  html = '';

  if (userNickname == data.leader && data.studyStatus == 'RECRUIT') {
    html += `
      <div id = "waitingList">< 참여 신청자 목록 >
      <table>
    `;
    for (let i = 0; i < teamMateList.length; i++) {
      if (teamMateList[i].teamMateStatus == 'WAITING') {
        html += `
          <tr>
            <td>
              <a id="waitingNickname" href="/user/mypage?userNickname=${teamMateList[i].userNickname}" style="margin-right: 5px;">
                <img id = "waitingProfile" src="${teamMateList[i].userprofileUrl}">
                ${teamMateList[i].userNickname}
              </a>
            </td>
            <td>
              <button onclick = "updateTeamMate(${teamMateList[i].id})" id = "updatebtn" class="btn btn-outline-secondary">수락</button>
              <button onclick = "deleteTeamMate(${teamMateList[i].id})" id = "updatebtn" class="btn btn-outline-secondary">거절</button>
            </td>
          </tr>
        `;
      }
    }

  }
  html += `
      </div>
    </table>
  `;
  studyDetail_right.innerHTML = html;
  // studyDetail-right[end]
}

function findTeamMateList(studyId) {
  console.log("findTeamMateList 실행");
  return fetch(`/${studyId}/teamMate/data`)
  .then(response => response.json());
}

function checkTeamMate(teamMateList) {
  isParticipation = false;
  isTeamMate = false;
  isDelete = false;
  cnt = 0;
  console.log("checkTeamMate 실행");
  for(let i = 0; i < teamMateList.length; i++){
    if(teamMateList[i].userNickname == userNickname) {   // 현재 유저가 팀원이라면, isParticipation은 true
      isParticipation = true;
      if(teamMateList[i].teamMateStatus == 'ACCEPTED'){  // 현재 유저가 팀원인 채, 수락된 상황이라면, isTeamMate은 true
        isTeamMate = true;
      }
    }
    if (teamMateList[i].teamMateStatus == 'ACCEPTED') {
      cnt += 1;
    }
  }
  if (cnt == 1) {    // 팀원이 오직 나 하나뿐이라면, 바로 탈퇴가 가능하다.
    isDelete = true;
  }
  console.log("isParticipation : " + isParticipation);
  console.log("isTeamMate : " + isTeamMate);
  console.log("isDelete : " + isDelete);

}

// onclick
function create() {
  console.log("create 실행");
  if (!confirm('스터디에 참가 신청하시겠습니까?')) {
    return false;
  }
  else {
    fetch(`/${studyId}/teamMate/create`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      }
    })
    .then(async () => {
      teamMateList = await findTeamMateList(studyId, userNickname);
      checkTeamMate(teamMateList);
      findStudyDetail(studyId);
    })
    alert('신청이 완료되었습니다.')
  }
}

function quit() {
  console.log("quit 실행");
  if(!confirm('스터디에서 탈퇴하시겠습니까?')) {
    return false;
  } else {
    fetch(`/${studyId}/teamMate/quit`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      }
    })
    .then(async () => {
      teamMateList = await findTeamMateList(studyId, userNickname);
      checkTeamMate(teamMateList);
      findStudyDetail(studyId);
    })
    alert('탈퇴가 완료되었습니다.');
  }
}

function updateTeamMate(data) {
  if (!confirm('참가 신청을 수락하시겠습니까?')){
    return false;
  }else {
    fetch(`/${studyId}/teamMate/update/${data}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      }
    })
    .then(async () => {
      teamMateList = await findTeamMateList(studyId, userNickname);
      checkTeamMate(teamMateList);
      findStudyDetail(studyId);
    })
  }
}

function deleteTeamMate(data){
  if (!confirm('참가 신청을 거절하시겠습니까?')) {
    return false;
  } else {
    fetch(`/${studyId}/teamMate/delete/${data}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      }
    })
    .then(async () => {
      teamMateList = await findTeamMateList(studyId, userNickname);
      checkTeamMate(teamMateList);
      findStudyDetail(studyId);
    })
  }
}
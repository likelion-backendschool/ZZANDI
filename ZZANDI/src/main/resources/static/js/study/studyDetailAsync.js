'use strict'

const studyDetail_left = document.querySelector(".studyDetail-left");
const studyDetail_top = document.querySelector(".studyDetail-top");
const studyDetail_center = document.querySelector(".studyDetail-center");
const studyDetail_right = document.querySelector(".studyDetail-right");
const studyDetail_bottom = document.querySelector(".studyDetail-bottom");
const acceptedTeamMate = document.querySelector(".acceptedTeamMate");
const studyView = document.querySelector(".studyView");
const updateRate = document.querySelector(".updateRate")

const studyId = document.querySelector(".studyId").value;
const userNickname = document.querySelector(".userNickname").value;
const studyDays = document.querySelector(".studyDays").value;
const studyPeriod = document.querySelector(".studyPeriod").value;
const studyRecommend = document.querySelector(".studyRecommend").value;

let teamMateList;
let studyDetail;

window.onload = async () => {
  teamMateList = await findTeamMateList(studyId, userNickname);
  checkTeamMate(teamMateList);
  studyDetail = await findStudyDetail(studyId);
  displayStudy(studyDetail, teamMateList);
}

let isParticipation = false;
let isTeamMate = false;
let isDelete = false;
let cnt = 0;

const patternList = Array.of('clouds', 'bees', 'dominos', 'pie', 'piano', 'crosses', 'floor', 'wiggle', 'bubbles', 'ticTac', 'zigZag', 'stripes', 'food', 'aztec')

async function findStudyDetail(studyId) {
  console.log("findStudyDetail 실행");
  return fetch(`/study/detail/${studyId}/study-data`)
  .then(response => response.json())
}

function calcEach(data, EachRate) {
  let Total = 0;
  if (data.studyType == 'BOOK') {
    Total = data.bookPage;
  }
  else {
    Total = data.lectureNumber;
  }

  return (EachRate / Total) * 100;
}

function displayStudy(data, teamMateList) {
  console.log("displayStudy 실행");
  // StudyDetail-left [start]
  let html = '';
  html += `
    <div>
      <p id = "studyTag"># ${data.studyTag}</p>
      <a href="/study/coverUpload/${studyId}">
        <img class="studyCover flex-shrink-0" style="border-radius: 1.3em" src="${data.studyCoverUrl}">
      </a>
  `;

  console.log("isParticipation : " + isParticipation);
  // 팀원이 아니며 아직 모집 중일 때,
  if (!isParticipation && data.studyStatus == 'RECRUIT') {
    html += `
      <button onclick = "create()" id = "participationbtn" class="btn btn-outline-primary mt-3">참가 신청</button>
    `;
  }else if (isParticipation && !isTeamMate && data.studyStatus == 'RECRUIT') {
    html += `
      <button disabled onclick = "create()" id = "participationbtn" class="btn btn-outline-primary mt-3">신청 완료</button>
    `;
  }

  // 팀원이지만 팀장이 아니며, 아직 진행 중이 아닌 경우,
  if (isTeamMate && (userNickname != data.leader) && (data.studyStatus == 'RECRUIT' || data.studyStatus == 'RECRUIT_COMPLETE')) {
    html += `
      <button onclick = "quit()" id="quitbtn" class="btn btn-outline-secondary mt-3">탈퇴하기</button>
    `;
  } else if (isTeamMate && (data.studyStatus == 'RECRUIT' || data.studyStatus == 'RECRUIT_COMPLETE') && userNickname == data.leader) {
    if (!isDelete) {
      html += `
      <button onclick = "clickDelegatebtn()" id="quitbtn" class="btn btn-outline-secondary mt-3">탈퇴하기</button>
      `;
    } else {
      html += `
        <a href="/study/delete/${studyId}" onclick = "if (!confirm('스터디를 삭제하시겠습니까?')) return false;" 
          id="quitbtn" class="btn btn-outline-secondary mt-3">삭제하기</a>`;
    }
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
          <i class="bi bi-people-fill me-1"></i>
          <span>인원 : ${data.acceptedStudyMember} / ${data.studyPeople}명</span>
      </div>
   `;

  if (data.studyStatus == 'RECRUIT') {
    html += `<p id = "recruit">모집 중</p>`;
  } else if (data.studyStatus == 'RECRUIT_COMPLETE') {
    html += `<p id = "recruit">모집 완료</p>`;
  } else if (data.studyStatus == 'PROGRESS') {
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
      <button id = "typeIcon" onclick = "showStudyContent()">
        <i class="bi bi-book-half" style="font-size: 1.5rem; margin-right : 5px;"></i>
        상세 정보
        ( ${data.studyStart} <i class="bi bi-arrow-right-short" style="font-size: 1.5rem;"></i> ${data.studyEnd} )
      </button>
      <div class = "study-content">
        <p>책 이름 : ${data.bookName}</p>
        <p>작가 : ${data.bookAuthor}</p>
        <p>출판사 : ${data.bookPublisher}</p>
        <p>책 쪽수 : ${data.bookPage} 페이지</p>
        <p>ISBN : ${data.bookIsbn}</p>
      </div>
      
    `;
  } else {
    html += `
      <button id = "typeIcon" onclick = "showStudyContent()">
        <i class="bi bi-play-btn" style="font-size: 1.5rem; margin-right : 5px;"></i>
        상세 정보 
        ( ${data.studyStart} <i class="bi bi-arrow-right-short" style="font-size: 1.5rem;"></i> ${data.studyEnd} )
      </button>
      <div class = "study-content">
        <p>강의 이름 : ${data.lectureName}</p>
        <p>강사진 : ${data.lecturer}</p>
        <p>강의 개수 : ${data.lectureNumber} 강</p>
      </div>
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
      </table>
    </div>
  `;
  studyDetail_right.innerHTML = html;
  // studyDetail-right[end]

  // studyDetail-bottom[start]
  const widthShow = Math.round(calcRate(studyPeriod, studyDays));
  html = '';
  html += ``;
  if (data.studyStatus == 'PROGRESS') {
    if (data.studyType == 'BOOK') {
      html += `<p class = "recommend"><i class="bi bi-bar-chart-fill" style="font-size: 1.3rem; margin-right : 5px;"></i>오늘의 권장 진도율</p>`;
    } else {
      html += `<p class = "recommend"><i class="bi bi-bar-chart-fill" style="font-size: 1.3rem; margin-right : 5px;"></i>오늘의 권장 진도율 : ~ ${studyRecommend}강</p>`;
    }

    html += `
    <div class = "d-flex mt-3 mb-3 align-items-center">
      <div class="progress">
        <div class="zzandi ${widthShow} shadow jupiter"></div>
      </div>
      <p class = "mb-0 ms-3">${widthShow}%</p>
    </div>
    
    <p class = "studyRate"><i class="bi bi-bar-chart-fill" style="font-size: 1.3rem; margin-right : 5px;"></i>우리의 달성률</p>
    <div class = "d-flex mt-3 mb-3 align-items-center">
      <div class="progress">
        <div class="achieve shadow jupiter2"></div>
      </div>
      <p class = "mb-0 ms-3">${data.studyRate}%</p>
    </div>
    `;
  }

  studyDetail_bottom.innerHTML = html;
  const zzandi = document.querySelector(".zzandi");
  function showRate() {
    const width = calcRate(studyPeriod, studyDays);
    zzandi.style.width = `${width}%`
  }
  showRate();
  // studyDetail-bottom[end]

  // studyView[start]
  html = '';

  html += `
  <div style = "color : dimgray;">
    <i class="bi bi-eye"></i>
    ${data.views}
  </div>
  `;
  studyView.innerHTML = html;
  //studyView[end]

  //updateRate[start]
  html= ``;

  html += `
  <form id="study_input" style="display:none">
    <input type="text" class="form-control"  placeholder="진도율 들어감">
  </form>
  `
  updateRate.innerHTML = html;
  //updateRate[end]

  // acceptedTeamMate[start]
  html = ``;

  for (let i = 0; i < teamMateList.length; i++) {
    if (teamMateList[i].teamMateStatus == 'ACCEPTED') {
      html += `
      <div>
        <a id="accepted" href="/user/mypage?userNickname=${teamMateList[i].userNickname}">
        <img id = "acceptedProfile" src="${teamMateList[i].userprofileUrl}">
      `;
      if (teamMateList[i].userNickname == userNickname) {
        html +=`
        <span class = "fw-bold">${teamMateList[i].userNickname}</span>
        </a>
        `;
      }else {
        html +=`
        <span>${teamMateList[i].userNickname}</span>
        </a>
        `;
      }

      if (teamMateList[i].userNickname == data.leader) {
        html += `<i class ="fa-solid fa-crown" style = "color : orange"></i>`;
      } else {
        if (teamMateList[i].teamMateDelegate != 'WAITING') {
          html += `
            <button id="delegatebtn" onclick = "delegate(${teamMateList[i].id})"  class = "btn btn-outline-secondary">
            <i class="bi bi-send" style="font-size: 1.1rem; margin-right : 3px;"></i>신청</button>
        `;
        } else if (teamMateList[i].teamMateDelegate == 'WAITING') {
          html += `
            <button disabled class = "btn btn-outline-secondary" id = "completebtn">
            <i class="bi bi-send" style="font-size: 1.1rem; margin-right : 3px;"></i>완료</button>
          `;
        }
      }
      html += `
        </div>
      `;
      // 개인 진도율 바 보이는 부분
      const eachWidth = calcEach(data, teamMateList[i].teamRate);

      html += `
        <div class = "d-flex mt-3 mb-5 align-items-center">
          <div class="progress">
            <div class="bar shadow ${patternList[i]}"></div>
          </div>
          <p class = "mb-0 ms-3">${eachWidth}%</p>
        </div>
      `;
      //
      acceptedTeamMate.innerHTML = html;

      const pattern = "." + patternList[i];

      const each = document.querySelector(pattern);

      each.style.width = `${eachWidth}%`;
    }
    // acceptedTeamMate[end]
  }
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
      studyDetail = await findStudyDetail(studyId);
      displayStudy(studyDetail, teamMateList);
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
      studyDetail = await findStudyDetail(studyId);
      displayStudy(studyDetail, teamMateList);
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
      studyDetail = await findStudyDetail(studyId);
      displayStudy(studyDetail, teamMateList);
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
      studyDetail = await findStudyDetail(studyId);
      displayStudy(studyDetail, teamMateList);
    })
  }
}

function clickDelegatebtn() {
  console.log("clickDelegatebtn 실행");
  if(!confirm('스터디에서 탈퇴하시려면, 다른 팀원에게 권한을 위임해주세요.\n팀원이 수락한 후 탈퇴가 완료됩니다.')) {
    return false;
  } else {
    showDelegatebtn();
  }
}

function delegate(data) {
  console.log("delegate 실행");
  if (!confirm('권한을 위임하시겠습니까?')) {
    return false;
  }
  else {
    fetch(`/${studyId}/teamMate/delegate/${data}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      }
    })
    .then(async () => {
      teamMateList = await findTeamMateList(studyId, userNickname);
      checkTeamMate(teamMateList);
      studyDetail = await findStudyDetail(studyId);
      displayStudy(studyDetail, teamMateList);
      showDelegatebtn()
    });
    alert('신청이 완료되었습니다. 해당 팀원이 수락한 후 탈퇴가 완료됩니다.');
  }
}

function showDelegatebtn() {
  console.log("showDelgatebtn 실행");
  let delegatebtnList = document.querySelectorAll('#delegatebtn');
  let completebtnList = document.querySelectorAll('#completebtn');
  delegatebtnList.forEach(delegatebtn => {
    delegatebtn.style.display = 'inline';
  });
  completebtnList.forEach(completebtn => {
    completebtn.style.display = 'inline';
  });
}

function showStudyContent() {
  if ($('.study-content').css('display') == 'none') {
    $('.study-content').show();
  } else {
    $('.study-content').hide();
  }
}

// 진도율 관련 함수
function calcRate(studyPeriod, studyDays) {
  return (studyDays / studyPeriod) * 100;
}

function toggleStudyInput() {
  const study_input = document.getElementById("study_input");

  if (study_input.style.display !== "none") {
    study_input.style.display = "none";
  }
  else {
    study_input.style.display = "inline";
  }
}


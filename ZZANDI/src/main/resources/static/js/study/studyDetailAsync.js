'use strict'

const studyDetail = document.querySelector(".studyDetail");
const studyId = document.querySelector(".studyId").value;
const userNickname = document.querySelector(".userNickname").value;

window.onload = () => {
  findTeamMateList(studyId, userNickname);
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
    displayStudy(data);
    dis(data);
  });
}

// <form th:action="@{|/${studies.id}/teamMate/create|}" method="post">
//   // <button type="submit"
//              onClick="if (!confirm('스터디에 참가 신청하시겠습니까?')) return false; else alert('신청이 완료되었습니다.')"
//              className="btn btn-outline-secondary"
//     //     th:if ="!${isParticipation} and ${studies.studyStatus.name() == 'RECRUIT'}">참가 신청</button>
//     // </form>
function displayStudy(data) {
  console.log("displayStudy 실행");

  let html = '';
  html += `
    <div>
      <h3># ${data.studyTag}</h3>
      <a href="/study/coverUpload/${studyId}">
        <img class="studyCover flex-shrink-0" src="${data.studyCoverUrl}">
      </a>
  `;

  console.log(isParticipation);
  if (!isParticipation && data.studyStatus == 'RECRUIT') {
    html += `
      <button onclick = "create()" class="participationbtn btn btn-outline-primary mt-3">참가 신청</button>
    `;
  }

  html += `</div>`
  studyDetail.innerHTML = html;
  // studyDetail.innerHTML = `
  // <table>
  //   <thead>
  //     <tr>
  //       <th>대표 이미지</th>
  //       <th>스터디 이름</th>
  //       <th>스터디 팀장</th>
  //       <th>스터디 모집된 팀원 수</th>
  //       <th>스터디 인원 수</th>
  //       <th>스터디 시작일</th>
  //       <th>스터디 종료일</th>
  //       <th>스터디 태그</th>
  //       <th>스터디 달성률</th>
  //     </tr>
  //     </thead>
  //     <tbody>
  //     <td>
  //       <img id="studyCover" src="${data.studyCoverUrl}">
  //       <div>
  //         <a href="/study/coverUpload/${studyId}">스터디 대표이미지 생성</a>
  //       </div>
  //     </td>
  //     <td>${data.studyTitle}</td>
  //     <td>${data.leader}</td>
  //     <td>${data.acceptedStudyMember}</td>
  //     <td>${data.studyPeople}</td>
  //     <td>${data.studyStart}</td>
  //     <td>${data.studyEnd}</td>
  //     <td>${data.studyTag}</td>
  //     <td>${data.studyRate}</td>
  //     </tbody>
  //     <thead>
  //     <tr class = "studyType-th">
  //     </tr>
  //     </thead>
  //     <tbody class="studyType-td">
  //     </tbody>
  //   </table>
  // `;
}

function dis(data) {
  console.log("dis 실행");
  // const studyType_th = document.querySelector('.studyType-th');
  // const studyType_td = document.querySelector('.studyType-td');
  // if (data.studyType == 'BOOK') {
  //   studyType_th.innerHTML = `
  //     <th>스터디 타입</th>
  //     <th>책 이름</th>
  //     <th>책 쪽수</th>
  //     <th>책 작가</th>
  //     <th>책 출판사</th>
  //     <th>책 ISBN</th>
  //   `;
  //
  //   studyType_td.innerHTML = `
  //     <td>${data.studyType}</td>
  //     <td>${data.bookName}</td>
  //     <td>${data.bookPage}</td>
  //     <td>${data.bookAuthor}</td>
  //     <td>${data.bookPublisher}</td>
  //     <td>${data.bookIsbn}</td>
  //   `;
  // } else {
  //   studyType_th.innerHTML = `
  //     <th>스터디 타입</th>
  //     <th>강의 이름</th>
  //     <th>강사 이름</th>
  //     <th>강의 개수</th>
  //   `;
  //   studyType_td.innerHTML = `
  //     <td>${data.studyType}</td>
  //     <td>${data.lectureName}</td>
  //     <td>${data.lecturer}</td>
  //     <td>${data.lectureNumber}</td>
  //   `;

}

function findTeamMateList(studyId) {
  console.log("findTeamMateList 실행");
  fetch(`/${studyId}/teamMate/data`)
  .then(response => response.json())
  .then(teamMateList => {
    checkTeamMate(teamMateList)
  })
}

function checkTeamMate(teamMateList) {
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
}

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
    .then(()=>{
      findTeamMateList(studyId, userNickname);
      findStudyDetail(studyId);
    })
    alert('신청이 완료되었습니다.')
  }
}
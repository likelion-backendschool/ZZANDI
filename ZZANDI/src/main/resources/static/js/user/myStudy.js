const waitingList = document.querySelector(".waitingList");
const leaderList = document.querySelector(".leaderList");

let zzandi = document.getElementById("zzandi").value;
console.log(zzandi);
if (zzandi == 0) {
  document.getElementById("zzandiImg").src = "https://cdn-icons-png.flaticon.com/512/932/932669.png";
}else if (zzandi < 20) {
  document.getElementById("zzandiImg").src = "/images/ms-icon-144x144.png";
}else if (zzandi < 40){
  document.getElementById("zzandiImg").src = "https://cdn-icons-png.flaticon.com/512/4139/4139066.png";
}else if (zzandi < 60){
  document.getElementById("zzandiImg").src = "https://cdn-icons-png.flaticon.com/512/3095/3095133.png";
}else if (zzandi < 80){
  document.getElementById("zzandiImg").src = "https://cdn-icons-png.flaticon.com/512/7101/7101456.png";
}else {
  document.getElementById("zzandiImg").src = "https://cdn-icons-png.flaticon.com/512/2582/2582652.png";
}

window.onload = () => {
  findWaitingStudyList();
  findDelegateStudyList();
}

function findWaitingStudyList() {
  console.log("findWaitingStudyList 실행");

  fetch(`/user/findWaitingStudyList`)
  .then(response => response.json())
  .then(data => displayWaitingStudyList(data));

}

function findDelegateStudyList() {
  console.log("findDelegateStudyList 실행");

  fetch(`/user/findDelegateStudyList`)
  .then(response => response.json())
  .then(data => displayDelegateStudyList(data));

}

function displayWaitingStudyList(data) {
  console.log("displayWaitingStudyList 실행");
  let html = '';

  if (data.length == 0) {
    html += `
    <td>신청한 스터디가 없습니다.</td>
    `;
  }else{
    html += `
    <tr class = "text-center">
      <td>해시태그</td>
      <td>스터디명</td>
      <td>상태</td>
      <td>시작일</td>
      <td>종료일</td>
      <td></td>
    </tr>
    `;
  }

  for (let i = 0; i < data.length; i++) {
    html += `
    <tr>
        <td class = "interest"># ${data[i].studyTag}</td>
        <td class = "title" onclick="location.href='/study/detail/${data[i].studyId}'" style = "cursor: pointer;">${data[i].studyTitle}</td>
    `;

    if(data[i].studyStatus == 'RECRUIT'){
      html +=`<td class = "recruit">모집 중</td>`;
    }else if (data[i].studyStatus == 'RECRUIT_COMPLETE') {
      html +=`<td class = "recruit">모집 완료</td>`;
    }
    html += `
        <td>${data[i].studyStart}</td>
        <td>${data[i].studyEnd}</td>
        <td>
          <button type ="submit" onclick="deleteTeamMate(${data[i].studyId})" class = "btn btn-outline-secondary">취소</button>
        </td>
      </tr>
    `;
  }

  waitingList.innerHTML = html;
}

function displayDelegateStudyList(data) {
  console.log("displayDelegateStudyList 실행");
  let html = '';

  if (data.length == 0) {
    html += `
    <td>권한 위임 신청이 없습니다.</td>
    `;
  }else{
    html += `
    <tr class = "text-center">
      <td>해시태그</td>
      <td>스터디명</td>
      <td>상태</td>
      <td>시작일</td>
      <td>종료일</td>
      <td></td>
    </tr>
    `;
  }

  for (let i = 0; i < data.length; i++) {
    html += `
    <tr>
        <td class = "interest"># ${data[i].studyTag}</td>
        <td class = "title2" onclick="location.href='/study/detail/${data[i].studyId}'" style = "cursor: pointer;">${data[i].studyTitle}</td>
    `;

    if(data[i].studyStatus == 'RECRUIT'){
      html +=`<td class = "recruit">모집 중</td>`;
    }else if (data[i].studyStatus == 'RECRUIT_COMPLETE') {
      html +=`<td class = "recruit">모집 완료</td>`;
    }
    html += `
        <td>${data[i].studyStart}</td>
        <td>${data[i].studyEnd}</td>
        <td>
          <button type ="submit" onclick="delegateAccept(${data[i].studyId})" class = "btn btn-outline-secondary">수락</button>
        </td>
        <td>
          <button type ="submit" onclick="delegateRefuse(${data[i].studyId})" class = "btn btn-outline-secondary">거절</button>
        </td>
      </tr>
    `;
  }

  leaderList.innerHTML = html;
}

async function deleteTeamMate(data) {
  console.log(data);
  const teamMateId = await findTeamMate(data);
  console.log(teamMateId);

  if (!confirm("스터디 신청을 취소하시겠습니까?")) {
    return false;
  }else {

    fetch(`/${data}/teamMate/delete/${teamMateId}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      }
    })
    .then(response => findWaitingStudyList())

    alert('취소가 완료되었습니다.');
  }
}

async function delegateAccept(data) {
  console.log(data);

  if (!confirm("팀장 위임 신청을 수락하시겠습니까?")) {
    return false;
  }else {

    fetch(`/${data}/teamMate/delegate`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      }
    })
    .then(response => {
      findWaitingStudyList();
      findDelegateStudyList();
    })

    alert('해당 스터디의 팀장이 되신 것을 축하드립니다.');
  }
}

async function delegateRefuse(data) {
  console.log(data);

  if (!confirm("팀장 위임 신청을 거절하시겠습니까?")) {
    return false;
  }else {

    fetch(`/${data}/teamMate/delegate/refuse`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      }
    })
    .then(response => {
      findWaitingStudyList();
      findDelegateStudyList();
    })

    alert('신청을 거절하였습니다.');
  }
}

async function findTeamMate(studyId) {
  console.log("findTeamMate");
  return fetch(`/${studyId}/teamMate/findTeamMate`)
  .then(response => response.json());
}
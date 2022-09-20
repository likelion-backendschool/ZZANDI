'use strict'

const list = document.querySelector(".studyList");
const pagination = document.querySelector('.pagination');

const st = document.querySelector(".st").value;
const ss = document.querySelector(".ss").value;
const kw = document.querySelector(".kw").value;
const page = document.querySelector(".page").value;

window.onload = () => {
  findStudyList(st, ss, kw, page);
}

function findStudyList(st, ss, kw, page) {
  console.log("findStudyList 실행");
  fetch(`/study/list-data?st=${st}&ss=${ss}&kw=${kw}&page=${page}`)
  .then(response => response.json())
  .then(data => {
        if(data.content.length === 0) {
          list.innerHTML = '<td colspan="6">등록된 스터디가 없습니다.</td>';
          return false;
        } else {
          history.pushState({page : page}, "", `/study/list/test?st=${st}&ss=${ss}&kw=${kw}&page=${page}`)
          displayStudyList(data);
          displayPagination(data);
        }
      });
}

function displayStudyList(studyList) {
  console.log("displayStudyList 실행");
  let html = '';
  html += `
    <table>
  `;

  for (let i = 0; i < studyList.pageable.pageSize; i += 3) {
    html += `<tr>`;
    for (let j = 0; j < 3; j++) {
      if (studyList.content[i+j]){
      html += `
          <td>  
        <a class = "link" href = "/study/detail/${studyList.content[i+j].studyId}">
            <div class = "studyBox">
              <div class = "studyBox-top d-flex align-items-baseline mb-1">
                <p id = "studyTag"># ${studyList.content[i+j].studyTag}</p>
                <div class = "d-flex ms-auto">
                  <div id = "people">
                    <i class="bi bi-people-fill me-1"></i>${studyList.content[i+j].acceptedStudyMember} / ${studyList.content[i+j].studyPeople}
                  </div>
      `;
      if (studyList.content[i+j].studyStatus == 'RECRUIT'){
        html += `<div class = "text-center ms-2"><p id = "recruit">모집 중</p></div>`;
      }else if (studyList.content[i + j].studyStatus == 'RECRUIT_COMPLETE') {
        html += `<div class = "text-center ms-2"><p id = "recruit">모집 완료</p></div>`;
      }else if (studyList.content[i + j].studyStatus == 'PROGRESS') {
        html += `<div class = "text-center ms-2"><p id = "progress">진행 중</p></div>`;
      }else{
        html += `<div class = "text-center ms-2"><p id = "complete">완료</p></div>`;
      }
      html += `
              </div>
            </div>
            <hr id = "line">
            <div class = "d-flex mt-2">
              <div>
                <img class = "studyCover" src="${studyList.content[i+j].studyCoverUrl}">
              </div>
              <div class = "w-100">
                <p id = "studyTitle">${studyList.content[i+j].studyTitle}</p>              
              </div>
            </div>
            <div class = "studyBox-bottom">
              <div id = "date">
                <i class="bi bi-calendar-range me-2"></i> ${studyList.content[i+j].studyStart} <i class="bi bi-arrow-right-short" style="font-size: 1.2rem;"></i> ${studyList.content[i+j].studyEnd}
              </div>
              <div id="view"><i class="bi bi-eye me-1"></i> ${studyList.content[i+j].views}</div>
            </div>
          </div>
        </a>
         </td>
      `;
      }
    }
    html += `</tr>`;
    }
    html += `
      </table>
    `;
    list.innerHTML = html;
}

function displayPagination(studyList) {
  console.log("displayPagination 실행");
  let pageNumber = studyList.pageable.pageNumber;   // 현재 페이지
  let totalPages = studyList.totalPages;            // 전체 페이지 수

  let startStudy = Math.floor((pageNumber) / 10) * 10;  // 시작 스터디
  let endStudy = startStudy + 10                                 // 끝 페이지
  console.log(startStudy);
  console.log(endStudy);
  if (endStudy > totalPages) endStudy = totalPages;

  let isFirst = studyList.first;     // 첫페이지인지
  let isLast = studyList.last;
  const firstDisabled = isFirst ? "disabled = 'disabled'" : '';
  const lastDisabled = isLast ? "disabled = 'disabled'" : '';

  let html = '';

  html += `
  <li class = "page-item">
    <button onclick="findStudyList(st, ss, kw, 0);" ${firstDisabled}>
      <i class="bi bi-chevron-bar-left"></i>
    </button>
  </li>
  <li class = "page-item">
    <button onclick="findStudyList(st, ss, kw, ${pageNumber - 1})" ${firstDisabled}>
      <i class="bi bi-chevron-left"></i>
    </button>
  </li>
  `;

  for (let i = startStudy; i < endStudy; i++) {
    const pageActive = (i === pageNumber) ? 'pageActive' : '';
    html += `
    <li class = "page-item">
      <button id = "${pageActive}" onclick = "findStudyList(st, ss, kw, ${i})">${i+1}</button>
    </li>
    `;
  }

  html += `
  <li class= "page-item">
    <button onclick="findStudyList(st, ss, kw, ${pageNumber + 1})" ${lastDisabled}>
      <i class="bi bi-chevron-right"></i>
    </button>
  </li>
  <li class = "page-item">
    <button onclick="findStudyList(st, ss, kw, ${totalPages - 1})" ${lastDisabled}>
      <i class="bi bi-chevron-bar-right"></i>
    </button>
  </li>
  `;

  pagination.innerHTML = html;
}

window.addEventListener('popstate', (e) => {
      const data = history.state;
      fetch(`/study/list-data?st=${st}&ss=${ss}&kw=${kw}&page=${page}`)
      .then(response => response.json())
      .then(data => {
        if(data.content.length === 0) {
          list.innerHTML = '<td colspan="6">등록된 스터디가 없습니다.</td>';
          return false;
        } else {
          displayStudyList(data);
          displayPagination(data);
        }
      })
    }
);
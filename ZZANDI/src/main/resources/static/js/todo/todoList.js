'use strict'

const listAll = document.querySelector(".listAll");
const listActive = document.querySelector(".listActive");
const listCompleted = document.querySelector(".listCompleted");

window.onload = () => {
    findAll();
}

// 게시물 검색
function findAll() {
    fetch("/todo/list-json")
        .then(response => response.json())
        .then(data => {
            console.log(data);
            displayItems(data);
        });
}

/**
 * 만들어진 list 목록을 <div>에 그려주는 함수
 */
function displayItems(data, type) {
    let htmlAll = "";
    let htmlActive = "";
    let htmlCompleted = "";

    for(let i = 0; i < data.length; i++) {
        if (data[i].type == 'DOING') {
            htmlAll +=`
                <li class="list-group-item d-flex align-items-center border-0 mb-2 rounded"
                    style="background-color: #f4f6f7;">

                    <div class="flex-grow-1">
                        <input class="form-check-input me-2" type="checkbox"/>
                        ${data[i].content}
                        </input>
                    </div>
                    <button type="button" class="btn-close" aria-label="Close"></button>
                </li>`
            htmlActive +=`
                <li class="list-group-item d-flex align-items-center border-0 mb-2 rounded"
                    style="background-color: #f4f6f7;">

                    <div class="flex-grow-1">
                        <input class="form-check-input me-2" type="checkbox"/>
                        ${data[i].content}
                        </input>
                    </div>
                    <button type="button" class="btn-close" aria-label="Close"></button>
                </li>`
        }
        else {
            htmlAll +=`
                <li class="list-group-item d-flex align-items-center border-0 mb-2 rounded"
                    style="background-color: #f4f6f7;">

                    <div class="flex-grow-1">
                        <input class="form-check-input me-2" type="checkbox" checked/>
                        ${data[i].content}
                        </input>
                    </div>

                    <button type="button" class="btn-close" aria-label="Close"></button>
                </li>`
            htmlCompleted +=`
                <li class="list-group-item d-flex align-items-center border-0 mb-2 rounded"
                    style="background-color: #f4f6f7;">

                    <div class="flex-grow-1">
                        <input class="form-check-input me-2" type="checkbox" checked/>
                        ${data[i].content}
                        </input>
                    </div>

                    <button type="button" class="btn-close" aria-label="Close"></button>
                </li>`
        }
    }


    console.log(htmlAll);
    listAll.innerHTML=htmlAll;
    listActive.innerHTML=htmlActive;
    listCompleted.innerHTML=htmlCompleted;
}

function changeType(id) {
    location.replace("/todo/change?id="+id);
    findAll();
}
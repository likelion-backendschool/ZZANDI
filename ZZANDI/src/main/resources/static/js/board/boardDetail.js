'use strict';

// 토스트 UI 뷰어
const Editor = toastui.Editor;
const viewer = new Editor.factory({
    el: document.querySelector('#viewer'),
    initialValue: document.querySelector(".board-content").value,
    viewer: true,
    plugins: [Editor.plugin.codeSyntaxHighlight],
    customHTMLRenderer: {
        htmlBlock: {
            iframe(node) {
                const link = node.attrs.src;
                return [
                    {
                        type: 'openTag',
                        tagName: 'iframe',
                        outerNewLine: true,
                        classNames: ['youtube'],
                        attributes: {
                            src: link
                        },
                    },
                    {
                        type: 'html',
                        content: node.childrenHTML
                    },
                    {
                        type: 'closeTag',
                        tagName: 'iframe',
                        outerNewLine: true
                    },
                ];
            },
        },
    },
});


function deleteBoard() {
    if (confirm("정말로 삭제하시겠습니까?") === false) {
        return false;
    }
    document.forms['form'].submit();
}

const studyId = document.querySelector(".study-id").value;
const currBoardId = document.querySelector(".board-id").value;
const fileBox = document.querySelector(".file-box");
fetch(`/${studyId}/board/file-list/${currBoardId}`)
    .then(data => data.json())
    .then(file => createThumbnail(file));

function createThumbnail(file) {
    fileBox.innerHTML = '';
    for (let f of file) {
        console.log(f);
        fileBox.innerHTML +=
        `<div style="position: relative; display: ${f.fileStatus === 'DELETE' ? 'none' : 'inline'};">
            <a href="/{studyId}/board/download/${f.id}">
                <img src="${f.fileUrl}" alt="upload file" class="file-img"/>
            </a>
            <a onclick="deleteFile(${studyId}, ${f.id})" class="file-del-btn" style="cursor: pointer">
                <i class="fa-sharp fa-solid fa-square-xmark" style="object-fit: cover; position: absolute; font-size: 16px; color: var(--bs-gray); right: 8px; top: 2px;"></i>
            </a>
        </div>`;
    }
}

function deleteFile(studyId, fileId) {
    if(confirm("정말로 삭제하시겠습니까?") === false) {
        return false;
    }

    fetch(`/${studyId}/board/delete/file/${fileId}`, {
        method: 'POST'
    }).then(() => {
        fetch(`/${studyId}/board/file-list/${currBoardId}`)
            .then(data => data.json())
            .then(file => {
                createThumbnail(file);
            });
    });
}
'use strict';

const editorContent = document.querySelector(".editorContent");

const Editor = toastui.Editor;
const editor = new Editor({
    el: document.querySelector('#editor'),
    height: '500px',
    initialEditType: 'markdown',
    previewStyle: 'vertical',
    initialValue: editorContent.value,
    previewHighlight: false,
    plugins: [Editor.plugin.codeSyntaxHighlight,
                Editor.plugin.colorSyntax,
                Editor.plugin.tableMergedCell],

    hooks: {
        addImageBlobHook: (image, callback) => {
            const formData = new FormData();
            formData.append('image', image);

            fetch('/change/url', {
                method: 'POST',
                body: formData,
            })
                .then(response => response.text())
                .then(url => callback(url, "대체 텍스트"));
        }
    },
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
                            width: 400,
                            height: 300,
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

const boardId = document.querySelector('.board-id').value;
const studyId = document.querySelector('.study-id').value;
const existFileBox = document.querySelector('.exist-file-box');
window.onload = () => createExistFiles(studyId, boardId);

document.querySelector("#file").addEventListener('change', (e) => upload(e.target.files));

document.querySelector('.exist-file-box').addEventListener('click', (e) => {
    const target = e.target;
    if (target.tagName !== 'IMG') {
        return false;
    }
    target.classList.toggle('selected');
});

document.querySelector('.return-btn').addEventListener('click', () => {
    // 현재 게시글의 모든 첨부 파일의 상태를 EXIST로 변경
    fetch(`/${studyId}/board/update/file/${boardId}`, {method: 'POST'})
        .then(result => console.log(result));
})

document.querySelector('#image_container').addEventListener('click', (e) => {
    const target = e.target;
    if (target.tagName !== 'IMG') {
        return false;
    }
    target.classList.toggle('selected');
});

function createExistFiles(studyId, boardId) {
    fetch(`/${studyId}/board/exist/file/${boardId}`)
        .then(data => data.json())
        .then(images => {
            existFileBox.innerHTML = '';
            let length = images.length;
            let existFileSize = 0; // 기존 파일들의 총 크기
            for(let img of images) {
                if(img.fileStatus !== 'DELETE') {
                    existFileSize += img.fileSize;
                    existFileBox.innerHTML += `<img src=${img.fileUrl} data-id=${img.id} alt='exist-img' class='file exist-file'>`;
                }
            }
        });
}

// 첨부된 이미지를 실제로 지우진 않고 화면에서 지우는 함수임
function deleteAllUploadFile() {
    let existFiles = Object.values(document.querySelectorAll('.file'));
    let fileList = document.querySelector('#file');
    if (!confirm("모든 첨부 파일을 삭제하시겠습니까?")) {
        return false;
    }

    for (let img of existFiles) {
        console.log(img.dataset.id);
        fetch(`/${studyId}/board/delete/file/${img.dataset.id}`, {method: 'POST'})
            .then(() => createExistFiles(studyId, boardId));
    }

    fileList.value = '';
    document.querySelector('#image_container').innerHTML = '';
}

function deleteSelectUploadFile() {
    let images = Object.values(document.querySelectorAll('.upload-img'));
    let existFiles = Object.values(document.querySelectorAll('.file'));

    let isCheck = false;
    for (let img of existFiles) {
        if (img.classList.contains('selected')) {
            isCheck = true;
        }
    }

    for (let img of images) {
        if (img.classList.contains('selected')) {
            isCheck = true;
        }
    }

    if (!isCheck) {
        alert('선택된 파일이 없습니다!');
        return false;
    }

    if (!confirm("선택한 첨부 파일을 삭제하시겠습니까?")) {
        return false;
    }

    // 기존 이미지 제거 코드
    existFiles = existFiles.filter(img => img.classList.contains('selected')); // 선택된 이미지 필터링
    for (let img of existFiles) {
        console.log(img.dataset.id);
        fetch(`/${studyId}/board/delete/file/${img.dataset.id}`, {method: 'POST'})
            .then(() => createExistFiles(studyId, boardId));
    }

    // 새로 추가한 이미지 제거
    const dataTransfer = new DataTransfer();
    let files = document.querySelector('#file').files;

    let fileArray = Array.from(files);
    images = images.filter(img => !img.classList.contains('selected'));
    fileArray = fileArray.filter(file => {
        for (let img of images) {
            if (img.dataset.name === file.name) {
                return file;
            }
        }
    });
    fileArray.forEach(file => dataTransfer.items.add(file));
    document.querySelector('#file').files = dataTransfer.files;
    upload(dataTransfer.files);
}

const allowedExtension = ['gif', 'png', 'jpeg', 'jpg', 'svg'];
function upload(files) {
    let size = 0;
    if(files.length > 0) {
        size = Array.from(files).map(file => file.size).reduce((sum, curr) => sum + curr);
    }
    if (size > 50000000) {
        alert('첨부 가능한 파일의 총 크기는 50MB 입니다!');
        document.querySelector('#file').value = '';
        return;
    }

    let totalSize = 0;
    let fileCount = 0;
    document.querySelector('#image_container').innerHTML = '';
    for (let image of files) {
        let reader = new FileReader();
        let extension = image.name.split('.')[1].toLowerCase();

        if(!allowedExtension.includes(extension)) {
            alert(`${extension} 형식의 파일은 업로드 할 수 없습니다!`);
            document.querySelector('#file').value = '';
            if(files.length === 1) {
                fileCount = 0;
                totalSize = 0;
            }
            break;
        }

        fileCount += 1;
        totalSize += image.size;

        reader.onload = function (e) {
            let img = document.createElement('img');
            img.setAttribute('class', 'upload-img');
            img.setAttribute('src', e.target.result);
            img.setAttribute('data-name', image.name);

            document.querySelector('#image_container').appendChild(img);
        };
        reader.readAsDataURL(image);
    }
}

function formatBytes(bytes, decimals = 2) {
    if (bytes === 0) return '0 Bytes';

    const k = 1024;
    const dm = decimals < 0 ? 0 : decimals;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];

    const i = Math.floor(Math.log(bytes) / Math.log(k));

    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
}

function validSubmit() {
    const category = document.querySelector(".category");
    const title = document.querySelector(".title");

    if (category.value === "") {
        alert("카테고리를 입력해주세요.");
        category.focus();
        return false;
    } else if (title.value === "") {
        alert("제목을 입력해주세요.");
        title.focus();
        return false;
    }

    editorContent.value = editor.getMarkdown();
    document.forms["form"].submit();
}
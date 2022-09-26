'use strict';

const studyId = document.querySelector('.study-id').value;

const Editor = toastui.Editor;
const editor = new Editor({
    el: document.querySelector('#editor'),
    height: '500px',
    initialEditType: 'markdown',
    previewStyle: 'vertical',
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

document.querySelector("#file").addEventListener('change', (e) => {
    let totalSize = 0;
    document.querySelector('#image_container').innerHTML = '';
    for (let image of e.target.files) {
        let reader = new FileReader();

        totalSize += image.size;

        reader.onload = function (e) {
            let img = document.createElement('img');
            img.setAttribute('src', e.target.result);
            document.querySelector('#image_container').appendChild(img);
        };
        reader.readAsDataURL(image);
    }

    document.querySelector(".file-count").innerHTML = `${e.target.files.length}개 첨부 됨 (${formatBytes(totalSize)} / 50MB)`;
});


function deleteAllUploadFile() {
    let fileList = document.querySelector('#file');
    if (fileList.value === '') {
        alert('첨부된 파일이 없습니다!');
        return false;
    }

    if (!confirm("첨부파일을 삭제하시겠습니까?")) {
        return false;
    }

    fileList.value = '';
    document.querySelector('.file-count').innerHTML = '';
    document.querySelector('#image_container').innerHTML = '';
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

    document.querySelector(".editorContent").value = editor.getMarkdown();
    document.forms["form"].submit();
}
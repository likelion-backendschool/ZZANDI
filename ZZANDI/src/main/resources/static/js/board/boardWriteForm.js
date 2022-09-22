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
    // const i = Math.floor(Math.log(totalSize) / Math.log(1024));
    // console.log(parseFloat((totalSize / Math.pow(2048, i)).toFixed(3)) + 'MB');
    document.querySelector(".file-count").innerHTML = `${e.target.files.length}개 첨부 됨 (${totalSize}Byte / 50MB)`;
});

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
'use strict';

const studyId = document.querySelector('.study-id').value;
const Editor = toastui.Editor;
const editor = new Editor({
    el: document.querySelector('#editor'),
    height: '700px',
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

            fetch('/change-url', {
                method: 'POST',
                body: formData,
            })
                .then(response => response.text())
                .then(url => callback(url, "대체 텍스트"));
        }
    }
});

// form 데이터 유효성 검사
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
'use strict';

// Toast UI Editor
const Editor = toastui.Editor;
const editor = new Editor({
    el: document.querySelector('#editor'),
    height: '700px',
    initialEditType: 'markdown',
    previewStyle: 'vertical',
    previewHighlight: false,
    plugins: [Editor.plugin.codeSyntaxHighlight]
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
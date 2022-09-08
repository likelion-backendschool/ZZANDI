'use strict';

const editorContent = document.querySelector(".editorContent");

const Editor = toastui.Editor;
const editor = new Editor({
    el: document.querySelector('#editor'),
    height: '700px',
    initialEditType: 'markdown',
    previewStyle: 'vertical',
    initialValue: editorContent.value,
    previewHighlight: false,
    plugins: [Editor.plugin.codeSyntaxHighlight, Editor.plugin.colorSyntax]
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

    editorContent.value = editor.getMarkdown();
    document.forms["form"].submit();
}
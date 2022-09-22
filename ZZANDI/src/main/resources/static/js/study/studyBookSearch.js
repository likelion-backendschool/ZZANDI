let bookTitleMap = new Map();
let bookAuthorMap = new Map();
let bookPublisherMap = new Map();
let bookIsbnMap = new Map();

$(document).ready(function () {

    $("#search").on('click', function () {
        var bookSearch = document.getElementById("bookSearch").value;
        if(bookSearch == ''){
            alert("책 정보를 입력해주세요");
            $('#bookSearch').focus();
            return false;
        }
        $.ajax({
            method: "GET",
            url: "http://localhost:8080/study/search/book",
            data: {
                query: $("#bookSearch").val(),
            },
        })
            .done(function (msg) {
                console.log(msg.item[0].title);
                console.log(msg.item[0].isbn13);
                $(".List").empty();
                for (var i = 0; i < msg.item.length; i++) {
                    var div = document.createElement('div');
                    div.setAttribute("class" , "image");
                    var img = document.createElement("img");
                    img.src = msg.item[i].cover;
                    div.appendChild(img);
                    document.querySelector(".List").appendChild(div);

                    bookTitleMap.set(i , msg.item[i].title);
                    bookAuthorMap.set(i , msg.item[i].author);
                    bookPublisherMap.set(i , msg.item[i].publisher);
                    bookIsbnMap.set(i , msg.item[i].isbn13);

                    var div = document.createElement('div');
                    div.setAttribute("class" , "btn-gradient green mini");
                    div.setAttribute("onclick", "bookInfo("+i+")");
                    var button = document.createElement("button");
                    button.append("선택");
                    div.appendChild(button);
                    document.querySelector(".List").appendChild(div);

                    var div = document.createElement('div');
                    div.setAttribute("id" , "text");
                    var title = document.createElement("p");
                    title.append(msg.item[i].title);
                    div.appendChild(title);
                    document.querySelector(".List").appendChild(div);

                }
            });
    });
});

function bookInfo(i){
    $('input[name=bookName]').attr('value', bookTitleMap.get(i));
    $('input[name=bookAuthor]').attr('value', bookAuthorMap.get(i));
    $('input[name=bookPublisher]').attr('value', bookPublisherMap.get(i));
    $('input[name=bookIsbn]').attr('value', bookIsbnMap.get(i));
    window.scrollTo(0,0);
}
window.onload = function (event) {
    document.getElementById("complete").onclick = function () {
        var bookName = document.getElementById("bookName").value;
        var bookAuthor = document.getElementById("bookAuthor").value;
        var bookPublisher = document.getElementById("bookPublisher").value;
        var bookIsbn = document.getElementById("bookIsbn").value;


        window.opener.document.getElementById("bookName").value = bookName;
        window.opener.document.getElementById("bookAuthor").value = bookAuthor;
        window.opener.document.getElementById("bookPublisher").value = bookPublisher;
        window.opener.document.getElementById("bookIsbn").value = bookIsbn;

        window.close();
    }
}
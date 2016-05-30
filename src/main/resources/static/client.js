function whitespaceBeforeTokenNeeded(elm) {
    return $.inArray(elm, [".", ","]) <= 0;
}

$(document).ready(function() {

    var textViewId;

    var process = function(paragraph, $tag) {
        var text = paragraph.text;
        var normalized = paragraph.normalized;
        text.forEach(function (elm, index) {
            var elmDef = {
                "text": elm
            };
            if (index in normalized) {
                elmDef["data-normalized"] = normalized[index];
                elmDef["class"] = "clickable";
            }
            if (whitespaceBeforeTokenNeeded(elm)) {
                $("<span> </span>").appendTo($tag);
            }
            $("<span></span>", elmDef).appendTo($tag);
        });
    };

    var getNextText = function() {
        $.get("/next", function (data) {
            textViewId = data.textViewId;
            var normalizedText = data.normalizedText;
            $textDiv = $("div#text");
            $textDiv.html('');

            if (normalizedText.caption) {
                $h3 = $("<h3></h3>").addClass('ui').addClass('header');
                $h3.appendTo($textDiv);
                process(normalizedText.caption, $h3);
            }
            normalizedText.paragraphs.forEach(function(paragraph) {
                $p = $("<p></p>");
                $p.appendTo($textDiv);
                process(paragraph, $p);
            });
        });
    };

    var onWordClick = function(event) {
        var $clickedSpan = $(event.target);
        var word = $clickedSpan.data("normalized");
        $.post("/" + textViewId + "/newword", {"normalized": word}, function () {
            console.log("ok");
        });
        $clickedSpan.addClass('clicked');
        $('span[data-normalized=' + word + ']').addClass('clicked');
    };

    var onNextTextButtonClick = function(event) {
        getNextText();
    };

    $('div#text').on("click", ".clickable", onWordClick);
    $('#next-text-button').on("click", onNextTextButtonClick);

    getNextText();
});
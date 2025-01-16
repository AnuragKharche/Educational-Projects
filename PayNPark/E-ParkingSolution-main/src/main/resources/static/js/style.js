$(document).ready(function() {
    // Get the current page and total pages
    var currentPage = parseInt($("#currentPage").val());
    var totalPages = parseInt($("#totalPages").val());

    // Set up the pagination
    $("#pagination").twbsPagination({
        totalPages: totalPages,
        visiblePages: 7,
        startPage: currentPage,
        first: "First",
        prev: "Previous",
        next: "Next",
        last: "Last",
        onPageClick: function (event, page) {
            // Handle the page click event
            // Example: redirect to the new page
            window.location.href = "/booking?page=" + (page - 1);
        }
    });
});

$('.carousel').carousel({
    interval: 200
});



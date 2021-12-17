const genreUrl = 'http://localhost:8080/genre';
const authorUrl = 'http://localhost:8080/author';
const bookUrl = 'http://localhost:8080/book';
let genres = [];
let authors = [];
let books = [];
let filters = []

let stateCount = 0;
const isStateLoaded = () => stateCount > 2;


function myFetch(url, fetchData, callback, awaitBody = true) {
    fetchData.headers = { 'Accept': 'application/json', 'Content-Type': 'application/json' };
    fetchData.body = JSON.stringify(fetchData.body);
    fetch(url, fetchData)
        .then(res => {
            if (awaitBody) {
                return res.json()
            }
            else {
                callback();
            }
        })
        .then(
            (response) => {
                callback(response);
            },
            (error) => {
                console.log(error);
            }
        )
}

function renderBookList() {
    $list = $('#Book-list');
    $list.empty();
    
    if(!books.length)
        return;

    books.forEach(book => {
        const $template = getTemplate(book);
        $list.append($template);
    })
}

function refreshData() {
    myFetch(bookUrl + '/all',
        { 'method': 'POST', 'body': filters },
        (result) => {
            books = result;

            if(!isStateLoaded()) {
                stateCount++;
                createModal();
                editModal();
            }

            renderBookList();
        });

    myFetch(genreUrl + '/all',
        { 'method': 'GET' },
        (result) => {
            genres = result;

            if(!isStateLoaded()) {
                stateCount++;
                genreSelect();
            }
        });
    myFetch(authorUrl + '/all',
        { 'method': 'GET' },
        (result) => {
            authors = result;

            if(!isStateLoaded()) {
                stateCount++;
                authorSelect();
            }
        });
}

function getFilterTemplate(name, filter) {
    return {
        'name': name,
        'values': filter
    };
}

//split string into array of substrings if separated by comma
function getSplitFilter(val) {
    if(val.indexOf(',') >= 0) {
        return val.split(',');
    }

    return [val];
}

function handleFilter() {
    let authorId = $('#filterAuthorSelect').val();
    let genreId = $('#filterGenreSelect').val();
    let title = $('#filterTitleInput').val();

    filters = [];

    if(authorId)
        filters.push(getFilterTemplate('author', [authorId])) 
    if(genreId)
        filters.push(getFilterTemplate('genre', [genreId]))
    if(title)
        filters.push(getFilterTemplate('title', getSplitFilter(title)));

    refreshData();
}

function handleCreate(item) {
    item.genre = genres.find(genre => genre.id === parseInt(item.genre));
    item.author = authors.find(author=> author.id === parseInt(item.author));

    myFetch(bookUrl,
        {
            'method': 'POST',
            'body': item
        },
        () => {
            refreshData();
			console.log("ok");
        })
}

function handleEdit(item) {
    //passing props genre and author contain entity id
    //then they are swtiched with related objects
    item.genre = genres.find(genre => genre.id === parseInt(item.genre));
    item.author = authors.find(author=> author.id === parseInt(item.author));

    myFetch(bookUrl,
        { 'method': 'PUT', 'body': item },
        () => {
            refreshData();
        })
}

function handleDelete(id) {
    myFetch(bookUrl + `/?id=${id}`,
        { 'method': 'DELETE' },
        () => {
            refreshData();
        },
        false)
}

function constant(val) {
    return val ?? '';
} 

function getTemplate(item) {
    const $template = `<tr>
                        <td class="book-id" scope="row">${item.id}</td>
                        <td class="book-genre">${item.genre.type}</td>
                        <td class="book-author">${item.author.name}</td>
						<td class="book-title">${item.title}</td>
                        
                        <td>
                            <button type="button" 
                                class="btn btn-primary" 
                                data-toggle="modal" 
                                data-target="#editModal" 
                                data-id=${item.id}
                                data-genre=${item.genre.id}
                                data-author=${item.author.id}
                                data-title=${item.title}
                                >Edit</button>
                            <button type="button" onClick="handleDelete(value)" value=${item.id} class="btn btn-danger">Delete</button>
                        </td>
                        </tr>`;

    return $template;
}

function editModal() {
    $('#editModal').on('show.bs.modal', function (e) {
        let btn = $(e.relatedTarget);
        let id = btn.data('id');
        let genre = btn.data('genre');
        let author = btn.data('author');
        let title = btn.data('title');
       

        $('#editGenreSelect').val(genre);
        $('#editAuthorSelect').val(author);
        $('.editTitleInput').val(title);
       
        $('.edit').data('id', id);
    })

    $('.edit').on('click', function () {
        let id = $(this).data('id');

        let genre = $('#editGenreSelect').val();
        let author = $('#editAuthorSelect').val();
        let title = $('.editTitleInput').val();
        

        handleEdit({ id, genre, author, title });

        $('#editModal').modal('toggle');
    })
}

function createModal() {
    $('#createModal').on('show.bs.modal', function () {
        $('#createGenreSelect').val('');
        $('#createAuthorSelect').val('');
        $('.createTitleInput').val('');
      
    })
    $('.create').on('click', function () {
        let genre = $('#createGenreSelect').val();
        let author = $('#createAuthorSelect').val();
        let title = $('.createTitleInput').val();
        console.log("test"); 
        handleCreate({ genre, author, title });
        $('#createModal').modal('toggle');
    })
}

function genreSelect() {
    var select = $('.select-genre');
    select.empty();
    select.append($('<option>', {
        value: null,
        text: ' '
    }))
    genres.forEach(genre => {
        select.append($('<option>', {
            value: genre.id,
            text: genre.type
        }));
    });
}

function authorSelect() {
    var select = $('.select-author');
    select.empty();
    select.append($('<option>', {
        value: null,
        text: ' '
    }))
    authors.forEach(author => {
        select.append($('<option>', {
            value: author.id,
            text: author.name
        }));
    });
}

refreshData();
const authorUrl = 'http://localhost:8080/author';
let authors = [];

function myFetch(url, fetchData, callback, awaitBody = true) {
    fetchData.headers = { 'Accept': 'application/json', 'Content-Type': 'application/json' };
    fetchData.body = JSON.stringify(fetchData.body);
    fetch(url, fetchData)
        .then(res => {
            if(awaitBody)
                return res.json()
            else
                callback();
        })
        .then(
            (response) => {
                callback(response);
            },
            // Note: it's important to handle errors here
            // instead of a catch() block so that we don't swallow
            // exceptions from actual bugs in components.
            (error) => {
                //catch err
            }
        )
}

function nullsToConstant(author) {
    author.lname = author.lname ?? '';
}

function renderAuthorList() {
    $list = $('#Author-list');
    $list.empty();
    authors.forEach(author => {
        nullsToConstant(author);
        const $template = getTemplate(author);
        $list.append($template);
    })
}

function refreshData() {
    myFetch(authorUrl + '/all',
        { 'method': 'GET' },
        (result) => {
            authors = result;
            renderAuthorList();
        })
}

function handleCreate(item) {
    myFetch(authorUrl,
        {
            'method': 'POST',
            'body': item
        },
        () => {
            refreshData();
        })
}

function handleEdit(item) {
    myFetch(authorUrl,
        { 'method': 'PUT', 'body': item },
        () => {
            refreshData();
        })
}

function handleDelete(id) {
    myFetch(authorUrl + `/?id=${id}`,
        { 'method': 'DELETE' },
        () => {
            refreshData();
        },
        false);
}

function getTemplate(item) {
    const $template = `<tr>
                        <td class="author-id" scope="row">${item.id}</th>
                        <td class="author-name">${item.name}</td>
                        <td class="author-lname">${item.lname}</td>
                        <td>
                            <button name="button" 
                                class="btn btn-primary" 
                                data-toggle="modal" 
                                data-target="#editModal" 
                                data-id=${item.id}
                                data-name=${item.name}
                                data-lname=${item.lname}>Edit</button>
                            <button name="button" onClick="handleDelete(value)" value=${item.id} class="btn btn-danger">Delete</button>
                        </td>
                        </tr>`;

    return $template;
}

function editModal() {
    $('#editModal').on('show.bs.modal', function (e) {
        let btn = $(e.relatedTarget);
        let id = btn.data('id');
        let name = btn.data('name');
        let lname = btn.data('lname');
        $('.editNameInput').val(name);
        $('.editLastNameInput').val(lname);

        $('.edit').data('id', id);
    })

    $('.edit').on('click', function () {
        let id = $(this).data('id');

        let name = $('.editNameInput').val();
        let lname = $('.editLastNameInput').val();

        handleEdit({ id, name, lname });

        $('#editModal').modal('toggle');
    })
}

function createModal() {
    $('#createModal').on('show.bs.modal', function (e) {
        $('.createNameInput').val('');
        $('.createLastNameInput').val('');
    })
    $('.create').on('click', function () {
        let name = $('.createNameInput').val();
        let lname = $('.createLastNameInput').val();

        handleCreate({ name, lname });
        $('#createModal').modal('toggle');
    })
}

refreshData();
setTimeout(editModal, 1);
setTimeout(createModal, 1);

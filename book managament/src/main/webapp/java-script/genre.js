const genreUrl = 'http://localhost:8080/genre';
let genres = [];

function myFetch(url, fetchData, callback, awaitBody = true) {
    fetchData.headers = { 'Accept': 'application/json', 'Content-Type': 'application/json' };
    fetchData.body = JSON.stringify(fetchData.body);
    fetch(url, fetchData)
        .then(res => {
            if (awaitBody)
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

function nullsToConstant(genres) {
    genres.description = genres.description ?? '';
}

function renderGenreList() {
    $list = $('#genre-list');
    $list.empty();
    genres.forEach(genre => {
        nullsToConstant(genre);
        const $template = getTemplate(genre);
        $list.append($template);
    })
}

function refreshData() {
    myFetch(genreUrl + '/all',
        { 'method': 'GET' },
        (result) => {
            genres = result;
            renderGenreList();
        })
}

function handleCreate(item) {
    myFetch(genreUrl,
        {
            'method': 'POST',
            'body': item
        },
        () => {
            refreshData();
        })
}

function handleEdit(item) {
    myFetch(genreUrl,
        { 'method': 'PUT', 'body': item },
        () => {
            refreshData();
        })
}

function handleDelete(id) {
    myFetch(genreUrl + `/?id=${id}`,
        { 'method': 'DELETE' },
        () => {
            refreshData();
        },
        false)
}

function getTemplate(item) {
    const $template = `<tr>
                        <td class="genre-id" scope="row">${item.id}</th>
                        <td class="genre-type">${item.type}</td>
                        <td class="genre-description">${item.description}</td>
                        <td>
                            <button type="button" 
                                class="btn btn-primary" 
                                data-toggle="modal" 
                                data-target="#editModal" 
                                data-id=${item.id}
                                data-type=${item.type}
                                data-description=${item.description}>Edit</button>
                            <button type="button" onClick="handleDelete(value)" value=${item.id} class="btn btn-danger">Delete</button>
                        </td>
                        </tr>`;

    return $template;
}

function editModal() {
    $('#editModal').on('show.bs.modal', function (e) {
        let btn = $(e.relatedTarget);
        let id = btn.data('id');
        let type = btn.data('type');
        let description = btn.data('description');
        $('.editTypeInput').val(type);
        $('.editDescriptionInput').val(description);

        $('.edit').data('id', id);
    })

    $('.edit').on('click', function () {
        let id = $(this).data('id');

        let type = $('.editTypeInput').val();
        let description = $('.editDescriptionInput').val();

        handleEdit({ id, type, description });

        $('#editModal').modal('toggle');
    })
}

function createModal() {
    $('#createModal').on('show.bs.modal', function (e) {
        $('.createTypeInput').val('');
        $('.createDescriptionInput').val('');
    })
    $('.create').on('click', function () {
        let type = $('.createTypeInput').val();
        let description = $('.createDescriptionInput').val();

        handleCreate({ type, description });
        $('#createModal').modal('toggle');
    })
}

refreshData();
setTimeout(editModal, 1);
setTimeout(createModal, 1);

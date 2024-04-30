
const baseEndPoint = "http://localhost:8080/Users";
const userList = document.querySelector(".list");
const errorBox = document.querySelector(".error-box");
const errorList = document.querySelector(".error-list");
const formAdd = document.querySelector(".form");
const formEdit = document.querySelector(".form-edit");
const modal = document.querySelector("#editModal");
const spanCloseModal = document.querySelector(".close-span");
const btnCloseModal = document.querySelector(".cancel-btn");

paramsName = ['id', 'firstName', 'lastName', 'birthDate', 'email', 'phone', 'address'];
const fieldsAdd = {};
const fieldsEdit = {};
paramsName.map(param => {
    fieldsAdd[param] = document.querySelector("[data-add-" + `${param.toLowerCase()}` + "]");
    fieldsEdit[param] = document.querySelector("[data-edit-" + `${param.toLowerCase()}` + "]");
});


async function editUser(id) {
    clearErrorBox();
    const url = `${baseEndPoint}/edit?id=${id}`;
    const params = {
        method: 'GET',
        headers: new Headers({
            'Content-Type': 'application/json; charset=UTF-8'
        })
    }
    let response = await fetch(url, params);

    if (response.ok) {
        const user = await response.json();
        Object.keys(user).map(key => {
            fieldsEdit[key].value = user[key];
        })
        modal.style.display = "block";
    } else {
        errorBox.style.display = "block";
        buildErrorList(await response.json());
    }
}

const onFormUpdateSubmit = (e) => {
    e.preventDefault();
    const dataParams= {};

    Object.keys(formEdit.elements).map(key => {
        if (paramsName.includes(key)) {
            dataParams[key] = formEdit.elements[key].value.trim();
        }
    });

    updateUser(dataParams);
}

formEdit.addEventListener("submit", onFormUpdateSubmit);

async function updateUser(dataParams) {
    clearErrorBox();
    const url = `${baseEndPoint}/edit`;
    const params = {
        method: 'PUT',
        body: JSON.stringify(dataParams),
        headers: new Headers({
            'Content-Type': 'application/json; charset=UTF-8'
        })
    }
    let response = await fetch(url, params);

    if (response.ok) {
        modal.style.display = "none";
        location.href=`${baseEndPoint}`;
    } else {
        errorBox.style.display = "block";
        buildErrorList(await response.json());
    }
}


const onFormAddSubmit = (e) => {
    e.preventDefault();
    const dataParams= {};
    Object.keys(formAdd.elements).map(key => {
        if (paramsName.includes(key)) {
            dataParams[key] = formAdd.elements[key].value.trim();
        }
    });

    addUser(dataParams);
    e.currentTarget.reset();
}

formAdd.addEventListener("submit", onFormAddSubmit);

async function addUser(dataParams) {
    clearErrorBox();
    const url = `${baseEndPoint}/create`;
    const params = {
        method: 'POST',
        body: JSON.stringify(dataParams),
        headers: new Headers({
            'Content-Type': 'application/json; charset=UTF-8'
        })
    }
    let response = await fetch(url, params);

    if (response.ok) {
        buildUserList([await response.json()]);
    } else {
        errorBox.style.display = "block";
        buildErrorList(await response.json());
    }
}

async function deleteUser(id) {
    const url = `${baseEndPoint}/delete`;
    const params = {
        method: 'DELETE',
        body: JSON.stringify({id: id}),
        headers: new Headers({
            'Content-Type': 'application/json; charset=UTF-8'
        })
    }
    let response = await fetch(url, params);

    if (response.ok) {
        location.href=`${baseEndPoint}`
    } else {
        buildErrorList(await response.json());
    }
}

const buildUserList = (users) => {
    userList.insertAdjacentHTML("beforeend", users.map(fillUserList).join(""));
}

const fillUserList = ({id, firstName, lastName, birthDate, email, phone, address}) => {
    return `
    <li class="list-item">
        <p class="item-id">${id}</p>
        <p class="item-el">${firstName} ${lastName}</p>
        <p class="item-el">${birthDate}</p>
        <p class="item-el">${email}</p>
        <p class="item-el">${phone}</p>
        <p class="item-el">${address}</p>
        <button class="btn" type="button" onclick="editUser('${id}')">Edit</button>
        <button class="btn" type="button" onclick="deleteUser('${id}')">Delete</button>
    </li>`;
};

async function getUsers() {
    const url = `${baseEndPoint}/list`;
    const params = {
        method: "GET",
        headers: {"Content-Type": "application/json; charset=UTF-8",},
    }
    let response = await fetch(url, params);

    if (response.ok) {
        buildUserList(await response.json());
    } else {
        buildErrorList(await response.json());
    }
}

const buildErrorList = ({errors}) => {

    if (Array.isArray(errors)) {
        errorList.insertAdjacentHTML("beforeend", errors.map(error => {
            return `<li>${error}</li>`;
        }).join(""));
    } else {
        Object.keys(errors).forEach(key => {
            errorList.insertAdjacentHTML("beforeend", errors[key].map(error => {
                return `<li><p>${key}</p>${error}</li>`;
            }).join(""));
        })
    }
}

const clearErrorBox = () => {
    errorBox.style.display = "none";
    errorList.innerHTML = "";
}
const clearFormFields = () => {
    Object.keys(fieldsEdit).map(key => {
        fieldsEdit[key].value = "";
    });
}



spanCloseModal.onclick = function() {
    modal.style.display = "none";
    clearFormFields();
}
btnCloseModal.onclick = function() {
    modal.style.display = "none";
    clearFormFields();
}

window.onclick = function(event) {
    if (event.target === modal) {
        modal.style.display = "none";
        clearFormFields();
    }
}


getUsers().then();
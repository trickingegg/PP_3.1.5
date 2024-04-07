console.log("Скрипт подключен и работает");
const tbody = document.getElementById("tbody");
const url = '/api/users';

async function getAdminPage() {
    let response = await fetch(url);
    if (response.ok) {
        response.json().then(usersJSONData => fillPage(usersJSONData, tbody));
    } else {
        alert(`HTTP Error: ${response.status}`);
    }
}

function fillPage(userData, tbody) {
    while (tbody.firstChild) {
        tbody.removeChild(tbody.firstChild);
    }

    userData.forEach(user => {
        let roleNames = [];
        user.roles.forEach(role => roleNames.push(" " + role.authority.replace('ROLE_', '')));

        const tRow = document.createElement("tr");
        tRow.innerHTML =
            `<td class="text-center">${user.id}</td>
             <td class="text-center">${user.name}</td>
             <td class="text-center">${user.surname}</td>
             <td class="text-center">${user.age}</td>
             <td class="text-center">${user.email}</td>
             <td class="text-center">${roleNames}</td>
             <td class="text-center">
                 <button class="btn btn-success btn-sm border rounded border-success"
                         type="button"
                         data-toggle="modal"
                         data-target="#editUser${user.id}"
                         data-user-id="${user.id}">Edit
                 </button>
             </td>
             <td class="text-center">
                 <button class="btn btn-danger btn-sm border rounded border-success"
                         type="button"
                         data-toggle="modal"
                         data-target="#deleteUser${user.id}"
                         data-user-id="${user.id}">Delete
                 </button>
            </td>`;
        tbody.append(tRow);
    });
}
document.addEventListener('DOMContentLoaded', function() {
    getAdminPage();
    tbody.addEventListener('click', function(event) {
        if (event.target.matches('.btn-success')) {
            const userId = event.target.getAttribute('data-user-id');
            editFormFill(userId);
        } else if (event.target.matches('.btn-danger')) {
            const userId = event.target.getAttribute('data-user-id');
            deleteForm(userId);
        }

    });
});


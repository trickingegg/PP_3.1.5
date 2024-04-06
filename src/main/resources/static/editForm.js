console.log("Скрипт подключен и работает");
const editForm = document.getElementById(`editForm`);
let idField, nameField, surnameField, emailField, ageField, loginField, passwordField, rolesSelect;

async function editFormFill(id) {
    console.log("editFormFill init");
    const userByIdURL = `/api/users/${id}`;
    let userResponse = await fetch(userByIdURL);
    if (userResponse.ok) {
        let user = await userResponse.json();
        idField = document.getElementById(`edit-id-${user.id}`);
        nameField = document.getElementById(`edit-name-${user.id}`);
        surnameField = document.getElementById(`edit-surname-${user.id}`);
        emailField = document.getElementById(`edit-email-${user.id}`);
        ageField = document.getElementById(`edit-age-${user.id}`);
        loginField = document.getElementById(`edit-username-${user.id}`);
        passwordField = document.getElementById(`edit-password-${user.id}`);
        rolesSelect = document.getElementById(`select-roles-${user.id}`);

        if(idField) idField.value = user.id;

        if(nameField) nameField.value = user.name;

        if(surnameField) surnameField.value = user.surname;

        if(emailField) emailField.value = user.email;

        if(ageField) ageField.value = user.age;

        if(loginField) loginField.value = user.username;
        await getRolesForEditForm(id);

        console.log("editFormFill end");
    } else {
        alert(`HTTP Error: ${userResponse.status}`);
    }
}

async function updateUser() {
    let userId = idField ? idField.value : null;
    if (!userId) {
        console.error('ID пользователя не найден');
        return;
    }

    const url = `/api/update-user/${userId}`;
    let roles = [];

    if (rolesSelect) {
        for (let i = 0; i < rolesSelect.options.length; i++) {
            if (rolesSelect.options[i].selected) roles.push({
                id: rolesSelect.options[i].value
            });
        }
    } else {
        console.error('Select с ролями не найден');
        return;
    }

    if (!nameField || !surnameField || !emailField || !ageField || !loginField || !passwordField) {
        console.error("Одно или несколько полей формы не найдены.");
        return;
    }

    let user = {
        id: userId,
        name: nameField.value,
        surname: surnameField.value,
        email: emailField.value,
        age: ageField.value,
        username: loginField.value,
        password: passwordField.value,
        roles: roles
    };

    const method = {
        method: 'PUT',
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(user)
    };

    try {
        let response = await fetch(url, method);
        if (response.ok) {
            $('#edit-close-btn').click();
            getAdminPage();
            console.log("Пользователь успешно обновлен");
        } else {
            let error = await response.text();
            throw new Error(error);
        }
    } catch (error) {
        console.error('Ошибка обновления пользователя:', error);
    }
}

async function getRolesForEditForm(userId) {
    console.log("getRolesForEditForm init");
    const getRolesURL = '/api/get-roles/';
    let rolesResponse = await fetch(getRolesURL);
    const rolesSelectId = `select-roles-${userId}`;
    const rolesSelect = document.getElementById(rolesSelectId);
    if (rolesResponse.ok) {
        let roles = await rolesResponse.json();
        let roleUser = roles[0];
        let roleAdmin = roles[1];
        // Убедитесь, что rolesSelect не равно null
        if(rolesSelect) {
            rolesSelect.options[0] = new Option('USER', roleUser.id);
            rolesSelect.options[1] = new Option('ADMIN', roleAdmin.id);
        }
    } else {
        alert(`HTTP Error: ${rolesResponse.status}`);
    }
}

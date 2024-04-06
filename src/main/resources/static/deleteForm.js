const deleteForms = document.getElementById(`deleteForms`);
let idDeleteField, nameDeleteField, surnameDeleteField, emailDeleteField, ageDeleteField, loginDeleteField;

async function deleteForm(id) {
    const userByIdURL = '/api/users/' + id;
    let userResponse = await fetch(userByIdURL);
    if (userResponse.ok) {
        let user = await userResponse.json();
        idDeleteField = document.getElementById(`delete-id-${user.id}`);
        nameDeleteField = document.getElementById(`delete-name-${user.id}`);
        surnameDeleteField = document.getElementById(`delete-surname-${user.id}`);
        emailDeleteField = document.getElementById(`delete-email-${user.id}`);
        ageDeleteField = document.getElementById(`delete-age-${user.id}`);
        loginDeleteField = document.getElementById(`delete-username-${user.id}`);

        if(idDeleteField) idDeleteField.value = user.id;
        if(nameDeleteField) nameDeleteField.value = user.name;
        if(surnameDeleteField) surnameDeleteField.value = user.surname
        if(emailDeleteField) emailDeleteField.value = user.email;
        if(ageDeleteField) ageDeleteField.value = user.age;
        if(loginDeleteField) loginDeleteField.value = user.username;

        await deleteUser();

    } else {
        alert(`HTTP Error, ${userResponse.status}`)
    }
}

async function deleteUser() {
    let userId = idDeleteField ? idDeleteField.value : null;
    if (!userId) {
        console.error('ID пользователя не найден');
        return;
    }
    let url = `/api/delete-user/${userId}`;

    let method = {
        method: 'DELETE',
        headers: {
            "Content-Type": "application/json"
        }
    }

    await fetch(url, method).then(() => {
        $('#delete-close-btn').click();
        getAdminPage();
    })
}
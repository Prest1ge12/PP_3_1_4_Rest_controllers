// Загрузка Атинтефицированного Юзера
document.addEventListener("DOMContentLoaded", function () {
    fetch('/api/users/authorise')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Полученные данные:', data);
            var userEmailElements = document.querySelectorAll(".authUserEmail");
            if (userEmailElements.length > 0) {
                userEmailElements.forEach(userEmailElement => {
                    userEmailElement.textContent = data.userEmail;
                });
            }

            var usernameElements = document.querySelectorAll(".authUserName");
            if (usernameElements.length > 0) {
                usernameElements.forEach(usernameElement => {
                    usernameElement.textContent = data.username;
                });
            }

            var userIdElements = document.querySelectorAll(".authUserId");
            if (userIdElements.length > 0) {
                userIdElements.forEach(userIdElement => {
                    userIdElement.textContent = data.id;
                });
            }

            var userSurnameElements = document.querySelectorAll(".authUserSurname");
            if (userSurnameElements.length > 0) {
                userSurnameElements.forEach(userSurnameElement => {
                    userSurnameElement.textContent = data.userSurname;
                });
            }

            var userAgeElements = document.querySelectorAll(".authUserAge");
            if (userAgeElements.length > 0) {
                userAgeElements.forEach(userAgeElement => {
                    userAgeElement.textContent = data.age;
                });
            }

            var userRolesElements = document.querySelectorAll(".authUserRoles");
            if (userRolesElements.length > 0) {
                // Устанавливаем текст для каждого элемента
                userRolesElements.forEach((roleElement) => {
                    roleElement.textContent = Array.from(data.roles).map(role => role.roleName).join(', ');
                });
            }

        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
});


// Функция для обработки нажатия кнопки "Изменить"
function handleUpdateButton(user) {
    const updateButton = document.createElement("button");
    updateButton.textContent = "Изменить";
    updateButton.className = "update-button btn btn-danger btn-sm";

    updateButton.onclick = function () {
        // Заполняем поля модального окна
        document.getElementById('editUserId').value = user.id;
        document.getElementById('username').value = user.username;
        document.getElementById('userEmail').value = user.userEmail;
        document.getElementById('userSurname').value = user.userSurname;
        document.getElementById('age').value = user.age;
        document.getElementById('password').value = user.password;

        // Получаем роли пользователя через Fetch API
        fetch(`/api/roles`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Сеть не отвечает: ' + response.statusText);
                }
                return response.json();
            })
            .then(allRoles => {
                const editRolesSelect = document.getElementById('editRoles');
                editRolesSelect.innerHTML = '';

                allRoles.forEach(role => {
                    const option = document.createElement('option');
                    option.value = role.id;
                    option.textContent = role.roleName;

                    if (user.roles && user.roles.some(userRole => userRole.id === role.id)) {
                        option.selected = true;
                    }
                    editRolesSelect.appendChild(option);
                });
            })
            .catch(error => {
                console.error('Ошибка при получении ролей:', error);
            });

        // Открываем модальное окно
        const editUserModal = new bootstrap.Modal(document.getElementById('editUserModal'));
        editUserModal.show();
    };

    return updateButton; // Возвращаем кнопку
}

// Функция для обработки нажатия кнопки "Удалить"
function handleDeleteButton(userId) {
    const deleteButton = document.createElement("button");
    deleteButton.textContent = "Удалить";
    deleteButton.className = "delete-button btn btn-primary btn-sm";

    deleteButton.onclick = function () {
        deleteUser(userId); // Ваша функция для удаления пользователя
    };

    return deleteButton; // Возвращаем кнопку
}


// Функция для загрузки пользователей
function fetchUsers() {
    fetch('/api/users')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            var allUsers = document.getElementById("all-users-tbody");

            if (allUsers) {
                allUsers.innerHTML = ""; // Очищаем контейнер перед добавлением

                data.forEach(user => {
                    var row = document.createElement("tr");

                    // Создаем ячейки для информации о пользователе
                    var idCell = document.createElement("td");
                    idCell.textContent = user.id;
                    row.appendChild(idCell);

                    var usernameCell = document.createElement("td");
                    usernameCell.textContent = user.username;
                    row.appendChild(usernameCell);

                    var emailCell = document.createElement("td");
                    emailCell.textContent = user.userEmail;
                    row.appendChild(emailCell);

                    var ageCell = document.createElement("td");
                    ageCell.textContent = user.age;
                    row.appendChild(ageCell);

                    var surnameCell = document.createElement("td");
                    surnameCell.textContent = user.userSurname;
                    row.appendChild(surnameCell);

                    var rolesCell = document.createElement("td");
                    rolesCell.innerHTML = user.roles.map(role => `<span>${role.roleName}</span>`).join(', ');
                    row.appendChild(rolesCell);

                    // Ячейка для кнопок
                    var updateAndDeleteCell = document.createElement("td");
                    updateAndDeleteCell.appendChild(handleUpdateButton(user)); // Добавляем кнопку "Изменить"
                    updateAndDeleteCell.appendChild(handleDeleteButton(user.id)); // Добавляем кнопку "Удалить"
                    row.appendChild(updateAndDeleteCell);

                    // Добавляем строку в контейнер таблицы
                    allUsers.appendChild(row);
                });
            } else {
                console.error('Элемент с id "all-users-tbody" не найден.');
            }
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
}

// Выполняем функцию при загрузке страницы
document.addEventListener("DOMContentLoaded", fetchUsers);


// Функция для удаления пользователя
function deleteUser(userId) {
    fetch(`/api/users/${userId}`, {method: 'DELETE'})
        .then(response => {
            if (response.ok) {
                // Успешно удалено, можно перезагрузить пользователей или обновить таблицу
                console.log(`Пользователь с ID ${userId} удален.`);
                // Вызовите обновление списка пользователей
                fetchUsers();
            } else {
                console.error('Ошибка при удалении пользователя:', response.statusText);
            }
        })
        .catch(error => {
            console.error('Ошибка при запросе на удаление:', error);
        });
}

// Переключатели вкладок
document.addEventListener("DOMContentLoaded", function () {

    var CreateUserBtn = document.querySelector("#create-user");
    console.log("Проверяем, существует ли кнопка создать нового юзера");
    if (CreateUserBtn) {
        CreateUserBtn.addEventListener("click", function () {
            clearAllUsers();
            document.getElementById("createUserForm").reset();
            // var createUserForm = document.getElementById("createUserForm").reset();
            // createUserForm.reset();
            console.log("Нажал на кнопку создать нового юзера");
        });
    }

    var ShowUserBtn = document.querySelector("#show-all-users");
    var allUsersThead = document.getElementById("all-users-thead");
    console.log("Проверяем, существует ли кнопка показать юзеров")
    if (ShowUserBtn) {
        ShowUserBtn.addEventListener("click", function () {
            allUsersThead.style.display = '';
            fetchUsers();
            console.log("Нажал на кнопку показать юзеров");
        });
    }
});

// Убрать всех юзеров
function clearAllUsers() {
    var allUsers = document.getElementById("all-users-tbody");
    var allUsersThead = document.getElementById("all-users-thead");
    if (allUsers) {
        allUsersThead.style.display = 'none';
        allUsers.innerHTML = ""; // Очищаем содержимое
    }
}

// СОЗДАНИЕ ПОЛЬЗОВАТЕЛЯ
document.getElementById('createUserForm').addEventListener('submit', function (event) {
    event.preventDefault(); // Отменяем стандартное поведение формы

    // Собираем данные пользователя
    const userData = {
        username: this.username.value,
        userEmail: this.userEmail.value,
        age: this.age.value,
        userSurname: this.userSurname.value,
        password: this.password.value,

    };

    const roles = Array.from(this.elements.createRoles)
        .filter(role => role.checked)
        .map(role => {
            if (role.value == 1) {
                return {roleName: 'ADMIN'};
            } else if (role.value == 2) {
                return {roleName: 'USER'};
            }
        });

    // Включаем роли в объект userData
    userData.roles = roles;

    console.log('Отправляемые данные:', JSON.stringify(userData)); // Логируем отправляемые данные
    console.log('Выбранные роли:', roles); // Логируем выбранные роли

    // Отправляем данные на сервер
    fetch(`/api/users`, { // Убрали лишнюю фигурную скобку
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(userData),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Ошибка при создании пользователя: ' + response.statusText);
            }
            return response.json(); // Обрабатываем ответ
        })
        .then(data => {
            console.log('Пользователь создан:', data);
            document.querySelector("#all-users-tab").click(); // Переходим на вкладку пользователей
        })
        .catch(error => {
            alert('Ошибка при создании пользователя: ' + error.message);
            console.error('Детали ошибки:', error);
        });
});

// Открыть модальное окно обновления пользователя
document.getElementById('updateForm').addEventListener('submit', function (event) {
    event.preventDefault(); // Отменяем стандартное поведение формы

    // Собираем данные из формы
    const userId = document.getElementById('editUserId').value;
    const userData = {
        editUserModalName: this.editUserModalName.value,
        editUserModalUserEmail: this.editUserModalUserEmail.value,
        editUserModalUserSurname: this.editUserModalUserSurname.value,
        editUserModalAge: this.editUserModalAge.value,
        editUserModalPassword: this.editUserModalPassword.value
    };

    // Получаем выбранные роли
    const roles = Array.from(this.elements.roles)
        .filter(role => role.checked)
        .map(role => role.value); // Получаем значения выбранных ролей

    console.log('Обновляемые данные:', JSON.stringify(userData)); // Логируем отправляемые данные
    console.log('Выбранные роли:', roles); // Логируем выбранные роли

    // Создаем параметры для запроса
    const params = new URLSearchParams();
    roles.forEach(roleId => params.append('roles', roleId));
    Object.keys(userData).forEach(key => {
        params.append(key, userData[key]);
    });

    // Отправляем данные на сервер
    fetch(`/api/users/${userId}?${params.toString()}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Ошибка при обновлении пользователя: ' + response.statusText);
            }
            return response.json(); // Обрабатываем ответ
        })
        .then(data => {
            console.log('Пользователь обновлен:', data);
            fetchUsers();
            document.querySelector(".btn-close").click();
        })
        .catch(error => {
            alert('Ошибка при обновлении пользователя: ' + error.message);
            console.error('Детали ошибки:', error);
        });
});
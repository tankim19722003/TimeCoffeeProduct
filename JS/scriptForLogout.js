function signOut() {
    localStorage.removeItem("userInfor");
    window.location.href = "../index.html";
}
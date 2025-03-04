const Logout = () => {

    const handleLogout = () => {

        console.log("Logout")
    }

    return (
        <button 
            className="remove-button"
            onClick={handleLogout}
            style={{margin: 0}}
        >
            Wyloguj
        </button>
    )
}

export default Logout
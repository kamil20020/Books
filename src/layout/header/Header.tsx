import { Link, useNavigate } from "react-router";
import logo from "../assets/logo.png"
import Navigation from "../Navigation";
import Logo from "../../components/Logo";
import LoginView from "./Login";
import Logout from "../../features/auth/Logout";
import { useAuthContext } from "../../context/AuthContext";

const Header = () => {

    const authContext = useAuthContext()
    const isUserLogged = useAuthContext().isUserLogged
    const username = authContext.user?.username

    return (
        <header>
            <div className="header-top">
                <Logo isLink/>
                <div className="header-actions">
                    {isUserLogged ?
                            <>
                                {username}
                                <Logout/> 
                            </>
                        : 
                            <LoginView/>
                    }
                </div>
            </div>
            <Navigation/>
        </header>
    )
}

export default Header;
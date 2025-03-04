import { Link, useNavigate } from "react-router";
import logo from "../assets/logo.png"
import Navigation from "../Navigation";
import Logo from "../../components/Logo";
import LoginView from "./Login";
import Logout from "../../features/auth/Logout";

const Header = () => {

    return (
        <header>
            <div className="header-top">
                <Logo isLink/>
                <div className="header-actions">
                <LoginView/>
                <Logout/>
                </div>
            </div>
            <Navigation/>
        </header>
    )
}

export default Header;
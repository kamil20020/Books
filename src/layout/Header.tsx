import { Link, useNavigate } from "react-router";
import logo from "../assets/logo.png"
import Navigation from "./Navigation";
import Logo from "../components/Logo";

const Header = () => {

    return (
        <header>
            <Logo isLink/>
            <Navigation/>
        </header>
    )
}

export default Header;
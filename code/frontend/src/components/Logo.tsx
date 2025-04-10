import { Link } from "react-router";
import logo from "../assets/logo.png"

const Logo = (props: {
    isLink?: boolean
}) => {

    if(props.isLink){

        return (
            <div id="logo">
                <Link to="/">
                    <img src={logo}/>
                </Link>
            </div>
        )
    }

    return (
        <img src={logo}/>
    )
}

export default Logo;
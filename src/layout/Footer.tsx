import { Link } from "react-router"
import Logo from "../components/Logo"
import { useAuthContext } from "../context/AuthContext"

const Footer = () => {

    const isUserAdmin = useAuthContext().isUserAdmin()

    return (
        <footer>
            <div className="footer-links">
                <Logo isLink/>
                <div className="footer-advanced-links">
                    {isUserAdmin && (
                        <>
                            <FooterLink link="/users" title="Użytkownicy"/>
                            <FooterLink link="/roles" title="Role"/>
                        </>
                    )}
                    <FooterLink link="/publishers" title="Wydawnictwa"/>
                    <FooterLink link="/authors" title="Autorzy"/>
                    <FooterLink link="/books" title="Książki"/>
                    <FooterLink link="/" title="Kontakt"/>
                </div>
            </div>
            <hr/>
            <div className="copyright">
                <p>&#169; Ebookpoint, 2025. Wszystkie prawa zastrzeżone.</p>
            </div>
        </footer>
    )
}

const FooterLink = (props: {
    link: string,
    title: string
}) => {

    return (
        <Link to={props.link}>{props.title}</Link>
    )
}

export default Footer;
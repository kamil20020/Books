import { Link } from "react-router";
import Menu, { MenuItem } from "../components/Menu";
import Icon from "../components/Icon";
import { useAuthContext } from "../context/AuthContext";

const NavigationItem = (props: {
    link: string,
    title: string
}) => {

    return (
        <Link key={props.link} to={props.link}>{props.title}</Link>
    )
}

const Navigation = () => {

    const isUserAdmin = useAuthContext().isUserAdmin()

    const menuItems: MenuItem[] = (() => {

        const newMenuItems: MenuItem[] = []
    
        if(isUserAdmin){
            newMenuItems.push(
                {
                    title: <NavigationItem key={1} link="/users" title="Użytkownicy"/>
                },
                {
                    title: <NavigationItem key={2} link="/roles" title="Role"/>
                }
            )
        }

        newMenuItems.push(
            {
                title: <NavigationItem key={4} link="/authors" title="Autorzy"/>
            },
            {
                title: <NavigationItem key={5} link="/" title="Książki"/>
            },
            {
                title: <NavigationItem key={3} link="/publishers" title="Wydawnictwa"/>
            },
        )
    
        return newMenuItems
    })()

    return (
        <nav>
            <SmallNavigation 
                key={"small"} 
                menuItems={menuItems}
            />
            <WideNavigation 
                key={"wide"} 
                menuItems={menuItems}
            />
        </nav>
    )
}

const SmallNavigation = (props: {
    menuItems: MenuItem[]
}) => {

    return (
        <div id="small-navigation">
            <Menu 
                title={<Icon iconName="menu"/>} 
                items={props.menuItems}
            />
        </div>
    )
}

const WideNavigation = (props: {
    menuItems: MenuItem[]
}) => {
    
    return (
        <div id="wide-navigation">
            {props.menuItems.map((item: MenuItem) => item.title)}
        </div>
    )
}

export default Navigation;
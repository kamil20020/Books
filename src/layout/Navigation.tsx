import { Link } from "react-router";
import Menu, { MenuItem } from "../components/Menu";
import Icon from "../components/Icon";

const NavigationItem = (props: {
    link: string,
    title: string
}) => {

    return (
        <Link key={props.link} to={props.link}>{props.title}</Link>
    )
}

const menuItems: MenuItem[] = [
    {
        title: <NavigationItem key={1} link="/users" title="Użytkownicy"/>
    },
    {
        title: <NavigationItem key={2} link="/roles" title="Role"/>
    },
    {
        title: <NavigationItem key={3} link="/publishers" title="Wydawnictwa"/>
    },
    {
        title: <NavigationItem key={4} link="/authors" title="Autorzy"/>
    },
    {
        title: <NavigationItem key={5} link="/books" title="Książki"/>
    }
]

const Navigation = () => {

    return (
        <nav>
            <SmallNavigation key={"small"}/>
            <WideNavigation key={"wide"}/>
        </nav>
    )
}

const SmallNavigation = () => {

    return (
        <div id="small-navigation">
            <Menu title={<Icon iconName="menu"/>} items={menuItems}/>
        </div>
    )
}

const WideNavigation = () => {

    return (
        <div id="wide-navigation">
            {menuItems.map((item: MenuItem) => item.title)}
        </div>
    )
}

export default Navigation;
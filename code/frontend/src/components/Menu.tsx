import { useState } from "react"

export interface MenuItem{
    title: string | React.ReactNode,
    onClick?: () => void
};

const Menu = (props: {
    title: string | React.ReactNode,
    items: MenuItem[]
}) => {

    const [isOpened, setIsOpened] = useState<boolean>(false)

    const switchMenu = () => {
        setIsOpened(!isOpened)
    }

    const handleItemClick = (item: MenuItem) => {

        if(item.onClick){
            item.onClick()
        }

        switchMenu()
    }

    return (
        <div className="menu">
            <div className="menu-title" onClick={switchMenu}>
                <p>{props.title}</p>
            </div>
            {isOpened && 
                <div className="menu-items">
                {props.items.map((item: MenuItem, index: number) => (
                    <p
                        key={index} 
                        className="menu-item"
                        onClick={() => handleItemClick(item)}
                    >
                        {item.title}
                    </p>
                ))}
            </div>
            }
        </div>
    )
}

export default Menu;
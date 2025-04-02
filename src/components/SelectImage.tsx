import { useState } from "react";
import Icon from "./Icon";
import Img from "./Img";
import ImgService from "../services/ImgService";

const SelectImage = (props: {
    img?: string,
    width?: number,
    height?: number,
    maxWidth?: number,
    maxHeight?: number,
    onChange: (newImg: string) => void
}) => {

    const [selectedImg, setSelectedImg] = useState<string | undefined>(props.img);

    const selectImg = () => {

        const input = document.createElement("input");
        input.type = "file";
    
        input.onchange = (e: any) => {

          const file = e.target.files[0];
    
          const reader = new FileReader();
          reader.readAsDataURL(file);
    
          reader.onload = (readerEvent: any) => {

            const img = readerEvent.target.result;

            setSelectedImg(img)
            props.onChange(ImgService.removeImgPrefix(img))
          };
        };
    
        input.click();
    }

    return (
        <div className="select-image" onClick={selectImg}>
            <Img data={selectedImg}/>
        </div>
    )
}

export default SelectImage;
import ImgService from "../services/ImgService"

const Img = (props: {
    data?: string,
    width?: number,
    height?: number,
    maxWidth?: number,
    maxHeight?: number,
    style?: React.CSSProperties
}) => {

    const height = props.height ? props.height : "auto"
    const width = props.width ? props.width : 180
    const maxWidth = props.maxWidth ? props.maxWidth : 500
    const maxHeight = props.maxHeight ? props.maxHeight : 500

    const img = props.data

    const noImgData = "https://media.istockphoto.com/id/1409329028/pl/wektor/brak-dost%C4%99pnego-obrazu-symbol-zast%C4%99pczy-miniatura-ikona-ilustracja.jpg?s=612x612&w=0&k=20&c=HQSo27lfrPAMaC-eUONkArhNeSYutr1Br1gwQy3Fmn0="

    const handleGetValidImg = () => {

        if(!img){
            return noImgData
        }

        if(ImgService.hasImgPrefix(img)){
            return img
        }

        return ImgService.addPrefixToImg(img)
    }

    return (
        <img 
            src={handleGetValidImg()}
            height={height}
            width={width}
            style={{
                maxHeight: maxHeight,
                maxWidth: maxWidth
            }}
        />
    )
}

export default Img;
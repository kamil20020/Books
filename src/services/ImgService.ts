export enum ImgType{
    JPG="jpg",
    PNG="png"
}

class ImgService{

    private imgPrefix = "data:image"

    addPrefixToImg(img: string, imgType: ImgType = ImgType.JPG){

        return `${this.imgPrefix}/${imgType};base64,${img}`
    }

    hasImgPrefix(img: string){

        return img.startsWith(this.imgPrefix)
    }

    removeImgPrefix(img: string){

        return img.split(",")[1]
    }
}

export default new ImgService()
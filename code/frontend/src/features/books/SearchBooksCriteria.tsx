import { useParams, useSearchParams } from "react-router";
import AddButton from "../../components/AddButton";
import DateSelector from "../../components/DateSelector";
import React from "react";

export interface SearchCriteria{
    phrase?: string,
    price?: number,
    publicationDate?: Date
}

const SearchBooksCriteria = () => {

    const [searchParams, setSearchParams] = useSearchParams()

    const handleOnChange = (name: string, value: any) => {

        searchParams.set(name, value)
        setSearchParams(searchParams)
    }

    const loadParam = (name: string, defaultValue: any): any => {

        if(!searchParams.has(name)){
            return defaultValue
        }

        return searchParams.get(name);
    }

    return (
        <React.Fragment>
            <input
                type="text"
                placeholder="Wyszukaj po tytule, autorze lub wydawcy"
                value={loadParam("phrase", "")}
                onChange={(event) => handleOnChange("phrase", event.target.value)}
            />
            <div className="additional-book-filters">
                <input
                    type="number" 
                    placeholder="Maks. cena"
                    min={0}
                    step={0.01}
                    value={loadParam("price", undefined)}
                    onChange={(event) => handleOnChange("price", event.target.value)}
                />
                <DateSelector
                    title="Wydano przed"
                    value={new Date(loadParam("publicationDate", new Date()))}
                    onChange={(newValue: Date) => handleOnChange("publicationDate", newValue)}
                />
            </div>
        </React.Fragment>
    )
}

export default SearchBooksCriteria;
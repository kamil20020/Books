import { useEffect, useState } from "react";
import Dropdown, { Option } from "../../components/Dropdown";
import Pageable from "../../models/api/request/pageable";
import PublisherService from "../../services/PublisherService";
import { AxiosResponse } from "axios";

const SelectPublisher = (props: {
    selectPublisherId: (newPublisherId: string) => void;
}) => {

    const handleGetPublishers = (pageable: Pageable): Promise<AxiosResponse<any, any>> => {

        return PublisherService.getPublishers(pageable)
    }

    const convertPublisherToOption = (publisher: any) => {
    
        return {
            key: publisher.id,
            value: publisher.id,
            placeholder: publisher.name
        }
    }

    const handleSelectPublisher = (newPublisherId: string) => {

        props.selectPublisherId(newPublisherId)
    }

    return (
        <Dropdown
            title="Główny wydawca"
            getOptions={handleGetPublishers}
            mapRowToOption={convertPublisherToOption}
            onSelect={handleSelectPublisher}
        />
    )
}

export default SelectPublisher;
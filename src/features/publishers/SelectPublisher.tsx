import { useEffect, useState } from "react";
import PaginationAPIDropdown from "../../components/PaginationAPIDropdown";
import Pageable from "../../models/api/request/pageable";
import PublisherService from "../../services/PublisherService";
import { AxiosResponse } from "axios";

const SelectPublisher = (props: {
    isRequired?: boolean;
    value?: string[];
    errorMessage?: string;
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
        <PaginationAPIDropdown
            key="publisher"
            title="Wydawca"
            value={props.value}
            isRequired={props.isRequired}
            errorMessage={props.errorMessage}
            getOptions={handleGetPublishers}
            mapRowToOption={convertPublisherToOption}
            onSelect={handleSelectPublisher}
        />
    )
}

export default SelectPublisher;
import { useEffect, useState } from "react";
import ContentHeader from "../components/ContentHeader";
import Publisher from "../models/api/response/publisher";
import PublisherService from "../services/PublisherService";
import Pageable from "../models/api/request/pageable";
import Page from "../models/api/response/page";
import "../features/publishers/publishers.css"
import PublisherView from "../features/publishers/PublisherView";

const Publishers = () => {

    const [publishers, setPublishers] = useState<Publisher[]>([])

    const pageSize = 2

    useEffect(() => {

        const pagination: Pageable = {
            page: 0,
            size: pageSize
        };

        PublisherService.getPublishers(pagination)
        .then((response) => {

            const pagedResponse: Page<Publisher> = response.data

            setPublishers(pagedResponse.content)

            console.log(pagedResponse.content)
        })
    }, [])
    
    return (
        <>
            <ContentHeader title="Wydawcy"/>
            <div className="publishers">
                {publishers.map((publisher: Publisher) => (
                    <PublisherView key={publisher.id} publisher={publisher}/>
                ))}
            </div>
        </>
    )
}

export default Publishers;
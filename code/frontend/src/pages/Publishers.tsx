import { useEffect, useRef, useState } from "react";
import ContentHeader from "../components/ContentHeader";
import Publisher from "../models/api/response/publisher";
import PublisherService from "../services/PublisherService";
import Pageable from "../models/api/request/pageable";
import Page from "../models/api/response/page";
import "../features/publishers/publishers.css"
import PublisherView from "../features/publishers/PublisherView";
import AddButton from "../components/AddButton";
import AddPublisher from "../features/publishers/AddPublisher";
import { useAuthContext } from "../context/AuthContext";

const Publishers = () => {

    const [publishers, setPublishers] = useState<Publisher[]>([])

    const isUserLogged = useAuthContext().isUserLogged

    const page = useRef<number>(0)
    const totalElements = useRef<number>(0)
    const pageSize = 2

    useEffect(() => {

       handleSearchAndAppend(0)
    }, [])

    const handleSearchAndAppend = (newPage: number) => {

        const pagination: Pageable = {
            page: newPage,
            size: pageSize
        };

        PublisherService.getPublishers(pagination)
        .then((response) => {

            const pagedResponse: Page<Publisher> = response.data

            setPublishers([...publishers, ...pagedResponse.content])
            page.current = newPage
            totalElements.current = pagedResponse.totalElements
        })
    }

    const handleAddPublisher = (newPublisher: Publisher) => {

        setPublishers([newPublisher, ...publishers])
        totalElements.current = totalElements.current + 1
    }

    const handleRemovePublisher = (removedPublisherId: string) => {

        const newPublishers = publishers
            .filter((publisher: Publisher) => publisher.id !== removedPublisherId)

        setPublishers(newPublishers)
        totalElements.current = totalElements.current - 1
    }
    
    return (
        <>
            <ContentHeader title="Wydawcy"/>
            <div className="publishers">
                {isUserLogged && <AddPublisher onAdd={handleAddPublisher}/>}
                {publishers.map((publisher: Publisher) => (
                    <PublisherView
                        key={publisher.id}
                        publisher={publisher} 
                        onRemove={handleRemovePublisher}
                    />
                ))}
                {publishers.length < totalElements.current &&
                    <AddButton
                        title="Załaduj dane"
                        onClick={() => handleSearchAndAppend(page.current + 1)}
                    />
                }
            </div>
        </>
    )
}

export default Publishers;
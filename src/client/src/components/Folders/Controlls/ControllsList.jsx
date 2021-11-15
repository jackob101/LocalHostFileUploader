import React from "react";
import { Link } from "react-router-dom";
import ControllItem from "./ControllItem";

const ControllsList = (props) => {
    return (
        <div className="d-flex flex-column mt-3">
            <ControllItem
                onClick={() => props.changeCurrentPath(0)}
                text=" < Go to root directory"
            />
            <ControllItem onClick={props.onGoToParentDir} text=" < Go up" />
            <Link
                to={{
                    pathname: "/note",
                    state: {
                        editing: false,
                        path: props.path.join("/"),
                    },
                }}
            >
                Create new note here
            </Link>
        </div>
    );
};

export default ControllsList;

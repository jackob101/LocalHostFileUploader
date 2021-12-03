import React from "react";
import { Link, useHistory } from "react-router-dom";
import ControllItem from "./ControllItem";

const ControllsList = (props) => {
    const history = useHistory();

    const onCreateNewNote = () => {
        history.push("/note", {
            editing: false,
            path: props.path.join("/"),
        });
    };

    return (
        <div className="d-flex flex-row">
            <ControllItem
                onClick={() => props.changeCurrentPath(0)}
                text="/"
                title="Go to root directory"
                disabled={props.path.length <= 0}
            />
            <ControllItem
                onClick={props.onGoToParentDir}
                text=".."
                title="Go back"
                disabled={props.path.length <= 0}
            />
            <ControllItem
                onClick={onCreateNewNote}
                text="+"
                title="Create new note"
            />
            <Link
                to={{
                    pathname: "/note",
                    state: {
                        editing: false,
                        path: props.path.join("/"),
                    },
                }}
            ></Link>
        </div>
    );
};

export default ControllsList;

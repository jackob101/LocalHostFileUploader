import React from "react";

const ControllItem = (props) => {
    return (
        <div className="d-flex flex-row">
            <button
                className=" btn btn-outline-primary border-0 m-2"
                onClick={props.onClick}
                title={props.title}
            >
                {props.text}
            </button>
        </div>
    );
};

export default ControllItem;

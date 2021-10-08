import React from "react";

const ControllItem = (props) => {
  return (
    <div className="d-flex flex-row">
      <button className=" btn btn-link border-0" onClick={props.onClick}>
        {props.text}
      </button>
    </div>
  );
};

export default ControllItem;

import React from "react";
import ControllItem from "./ControllItem";

const ControllsList = (props) => {
  return (
    <div className="d-flex flex-column mt-3">
      <ControllItem
        onClick={() => props.changeCurrentPath(0)}
        text=" < Go to root directory"
      />
      <ControllItem onClick={props.onGoToParentDir} text=" < Go up" />
    </div>
  );
};

export default ControllsList;

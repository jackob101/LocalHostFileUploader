import React, { useRef, useState } from "react";
import ToUploadList from "./ToUploadList";

const UploadPanel = (props) => {
  const [files, setFiles] = useState([]);
  const filesRef = useRef();

  const onFilesChange = (event) => {
    setFiles(Array.from(event.target.files));
  };

  return (
    <div className="d-flex flex-column p-3" style={{ width: "30%" }}>
      <form
        className="d-flex flex-column"
        onSubmit={(event) => props.onSubmit(event, files)}
        encType="multipart/form-data"
      >
        <label htmlFor="formFile" className="form-label fs-2 text-center">
          Upload file
        </label>
        <input
          className="form-control"
          type="file"
          id="formFile"
          onChange={onFilesChange}
          multiple
          ref={filesRef}
        ></input>
        <input
          className="btn btn-outline-primary mt-3"
          type="submit"
          value="Submit"
        />
        <label
          htmlFor="formFile"
          className="text-secondary"
          style={{ fontSize: "0.8em" }}
        >
          Files will be uploaded to directory you are currently in
        </label>
        <ToUploadList files={files} />
      </form>
    </div>
  );
};

export default UploadPanel;

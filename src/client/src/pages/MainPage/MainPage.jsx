import axios from "axios";
import React, { useEffect, useState } from "react";
import ControllsList from "../../components/Folders/Controlls/ControllsList";
import FilesList from "../../components/Folders/FilesList/FilesList";
import PathBreadcrumb from "../../components/Folders/PathBreadcrumb/PathBreadcrumb";
import UploadPanel from "../../components/Folders/UploadPanel/UploadPanel";

const MainPage = () => {
  const [files, setFiles] = useState([]);
  const [path, setPath] = useState([]);

  useEffect(() => {
    axios
      .get("https://localhost:8443/api/getFiles", {
        params: {
          path: path.join("/"),
        },
      })
      .then((response) => {
        setFiles(response.data.files);
      });
  }, [path]);

  const changeCurrentPath = (pathIndex) => {
    setPath(path.splice(0, pathIndex));
  };

  const onEnterDirectory = (directory) => {
    const newPath = [...path, directory];
    setPath(newPath);
  };

  const onGoToParentDir = () => {
    let newPath = [...path];
    newPath.pop();
    setPath(newPath);
  };

  const onFilesSubmit = (event, files) => {
    event.preventDefault();
    let formData = new FormData();
    files.map((entry) => {
      formData.append("files", entry);
    });
    console.log(path.join("/"));
    formData.append("path", path.join("/"));

    axios
      .post("https://localhost:8443/api/upload", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      })
      .then((response) => console.log(response));
  };

  return (
    <div className="d-flex flex-row flex-grow-1">
      <UploadPanel onSubmit={onFilesSubmit} />
      <div className="bg-secondary" style={{ width: "1px" }}></div>
      <div className="d-flex flex-column mx-auto" style={{ width: "70%" }}>
        <div className="p-3">
          <PathBreadcrumb path={path} changeCurrentPath={changeCurrentPath} />
        </div>
        <div
          className="bg-secondary"
          style={{ width: "100%", height: "1px" }}
        ></div>
        <div className="p-3">
          <ControllsList
            changeCurrentPath={changeCurrentPath}
            onGoToParentDir={onGoToParentDir}
          />

          <FilesList
            path={path}
            files={files}
            changeCurrentPath={changeCurrentPath}
            onGoToParentDir={onGoToParentDir}
            onEnterDirectory={onEnterDirectory}
          />
        </div>
      </div>
    </div>
  );
};

export default MainPage;

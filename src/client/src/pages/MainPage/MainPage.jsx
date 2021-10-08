import axios from "axios";
import React, { useEffect, useState } from "react";
import ControllsList from "../../components/Folders/Controlls/ControllsList";
import FilesList from "../../components/Folders/FilesList/FilesList";
import PathBreadcrumb from "../../components/Folders/PathBreadcrumb/PathBreadcrumb";

const MainPage = () => {
  const [files, setFiles] = useState([]);
  const [path, setPath] = useState([]);

  useEffect(() => {
    console.log(path);
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

  return (
    <div>
      <div className="d-flex flex-column mx-auto" style={{ width: "45%" }}>
        <PathBreadcrumb path={path} changeCurrentPath={changeCurrentPath} />
        <hr className="flex-grow-1 my-0" />
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
  );
};

export default MainPage;

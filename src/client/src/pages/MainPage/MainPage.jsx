import React from "react";
import ControllsList from "../../components/Folders/Controlls/ControllsList";
import FilesList from "../../components/Folders/FilesList/FilesList";
import NewDirectoryForm from "../../components/Folders/NewDirectoryForm";
import PathBreadcrumb from "../../components/Folders/PathBreadcrumb/PathBreadcrumb";
import UploadPanel from "../../components/Folders/UploadPanel/UploadPanel";
import useListService from "../../utils/FileListUtils";

const MainPage = () => {
  const {
    files,
    path,
    onFilesSubmit,
    changeCurrentPath,
    onGoToParentDir,
    onEnterDirectory,
    onCreateNewFolder,
  } = useListService();

  return (
    <div className="container h-100">
      <div className="row h-100">
        <div className="col-md-4 col-8">
          <UploadPanel onSubmit={onFilesSubmit} />
        </div>
        <div className="col d-flex flex-column border-start border-5  border-end">
          <div className="flex-grow-1 d-flex flex-column">
            <div className="p-2 border-bottom border-3">
              <PathBreadcrumb
                path={path}
                changeCurrentPath={changeCurrentPath}
              />
            </div>
            <div className="d-flex flex-column flex-grow-1">
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
            <div className="border-3 border-top p-3" style={{ height: "10%" }}>
              <NewDirectoryForm onCreateNewFolder={onCreateNewFolder} />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MainPage;

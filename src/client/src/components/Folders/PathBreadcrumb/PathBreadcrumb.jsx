import React from "react";

const PathBreadcrumb = ({ path, changeCurrentPath }) => {
    return (
        <nav aria-label="breadcrumb">
            <ol className="breadcrumb my-2">
                <li
                    className={
                        "breadcrumb-item " + (path.length === 0 ? "active" : "")
                    }
                    aria-current="page"
                >
                    <button
                        value={"/"}
                        onClick={() => changeCurrentPath(1)}
                        className="btn btn-link p-0 m-0"
                        disabled={path.length === 0}
                    >
                        Root
                    </button>
                </li>
                {path.map((entry, key) => {
                    return (
                        <li
                            key={key}
                            className="breadcrumb-item"
                            aria-current="page"
                        >
                            <button
                                value={key}
                                onClick={() => changeCurrentPath(key + 1)}
                                className="btn btn-link p-0 m-0"
                                disabled={path.length - 1 === key}
                            >
                                {entry}
                            </button>
                        </li>
                    );
                })}
            </ol>
        </nav>
    );
};

export default PathBreadcrumb;

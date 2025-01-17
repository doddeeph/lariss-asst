import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './product-details.reducer';

export const ProductDetails = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const productDetailsList = useAppSelector(state => state.productDetails.entities);
  const loading = useAppSelector(state => state.productDetails.loading);
  const totalItems = useAppSelector(state => state.productDetails.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="product-details-heading" data-cy="ProductDetailsHeading">
        <Translate contentKey="larissAsstApp.productDetails.home.title">Product Details</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="larissAsstApp.productDetails.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/product-details/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="larissAsstApp.productDetails.home.createLabel">Create new Product Details</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {productDetailsList && productDetailsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="larissAsstApp.productDetails.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="larissAsstApp.productDetails.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('price')}>
                  <Translate contentKey="larissAsstApp.productDetails.price">Price</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('price')} />
                </th>
                <th className="hand" onClick={sort('thumbnail')}>
                  <Translate contentKey="larissAsstApp.productDetails.thumbnail">Thumbnail</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('thumbnail')} />
                </th>
                <th>
                  <Translate contentKey="larissAsstApp.productDetails.product">Product</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="larissAsstApp.productDetails.description">Description</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="larissAsstApp.productDetails.color">Color</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="larissAsstApp.productDetails.processor">Processor</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="larissAsstApp.productDetails.memory">Memory</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="larissAsstApp.productDetails.storage">Storage</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="larissAsstApp.productDetails.screen">Screen</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="larissAsstApp.productDetails.connectivity">Connectivity</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="larissAsstApp.productDetails.material">Material</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="larissAsstApp.productDetails.caseSize">Case Size</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="larissAsstApp.productDetails.strapColor">Strap Color</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="larissAsstApp.productDetails.strapSize">Strap Size</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productDetailsList.map((productDetails, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/product-details/${productDetails.id}`} color="link" size="sm">
                      {productDetails.id}
                    </Button>
                  </td>
                  <td>{productDetails.name}</td>
                  <td>{productDetails.price}</td>
                  <td>{productDetails.thumbnail}</td>
                  <td>
                    {productDetails.product ? <Link to={`/product/${productDetails.product.id}`}>{productDetails.product.name}</Link> : ''}
                  </td>
                  <td>
                    {productDetails.description ? (
                      <Link to={`/description/${productDetails.description.id}`}>{productDetails.description.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{productDetails.color ? <Link to={`/color/${productDetails.color.id}`}>{productDetails.color.name}</Link> : ''}</td>
                  <td>
                    {productDetails.processor ? (
                      <Link to={`/processor/${productDetails.processor.id}`}>{productDetails.processor.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {productDetails.memory ? <Link to={`/memory/${productDetails.memory.id}`}>{productDetails.memory.name}</Link> : ''}
                  </td>
                  <td>
                    {productDetails.storage ? <Link to={`/storage/${productDetails.storage.id}`}>{productDetails.storage.name}</Link> : ''}
                  </td>
                  <td>
                    {productDetails.screen ? <Link to={`/screen/${productDetails.screen.id}`}>{productDetails.screen.name}</Link> : ''}
                  </td>
                  <td>
                    {productDetails.connectivity ? (
                      <Link to={`/connectivity/${productDetails.connectivity.id}`}>{productDetails.connectivity.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {productDetails.material ? (
                      <Link to={`/material/${productDetails.material.id}`}>{productDetails.material.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {productDetails.caseSize ? (
                      <Link to={`/case-size/${productDetails.caseSize.id}`}>{productDetails.caseSize.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {productDetails.strapColor ? (
                      <Link to={`/strap-color/${productDetails.strapColor.id}`}>{productDetails.strapColor.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {productDetails.strapSize ? (
                      <Link to={`/strap-size/${productDetails.strapSize.id}`}>{productDetails.strapSize.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/product-details/${productDetails.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/product-details/${productDetails.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/product-details/${productDetails.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="larissAsstApp.productDetails.home.notFound">No Product Details found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={productDetailsList && productDetailsList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default ProductDetails;

import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-details.reducer';

export const ProductDetailsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productDetailsEntity = useAppSelector(state => state.productDetails.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productDetailsDetailsHeading">
          <Translate contentKey="larissAsstApp.productDetails.detail.title">ProductDetails</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productDetailsEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="larissAsstApp.productDetails.name">Name</Translate>
            </span>
          </dt>
          <dd>{productDetailsEntity.name}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="larissAsstApp.productDetails.price">Price</Translate>
            </span>
          </dt>
          <dd>{productDetailsEntity.price}</dd>
          <dt>
            <span id="thumbnail">
              <Translate contentKey="larissAsstApp.productDetails.thumbnail">Thumbnail</Translate>
            </span>
          </dt>
          <dd>{productDetailsEntity.thumbnail}</dd>
          <dt>
            <Translate contentKey="larissAsstApp.productDetails.product">Product</Translate>
          </dt>
          <dd>{productDetailsEntity.product ? productDetailsEntity.product.name : ''}</dd>
          <dt>
            <Translate contentKey="larissAsstApp.productDetails.description">Description</Translate>
          </dt>
          <dd>{productDetailsEntity.description ? productDetailsEntity.description.name : ''}</dd>
          <dt>
            <Translate contentKey="larissAsstApp.productDetails.color">Color</Translate>
          </dt>
          <dd>{productDetailsEntity.color ? productDetailsEntity.color.name : ''}</dd>
          <dt>
            <Translate contentKey="larissAsstApp.productDetails.processor">Processor</Translate>
          </dt>
          <dd>{productDetailsEntity.processor ? productDetailsEntity.processor.name : ''}</dd>
          <dt>
            <Translate contentKey="larissAsstApp.productDetails.memory">Memory</Translate>
          </dt>
          <dd>{productDetailsEntity.memory ? productDetailsEntity.memory.name : ''}</dd>
          <dt>
            <Translate contentKey="larissAsstApp.productDetails.storage">Storage</Translate>
          </dt>
          <dd>{productDetailsEntity.storage ? productDetailsEntity.storage.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/product-details" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-details/${productDetailsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductDetailsDetail;

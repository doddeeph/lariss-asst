import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './order-product.reducer';

export const OrderProductDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const orderProductEntity = useAppSelector(state => state.orderProduct.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderProductDetailsHeading">
          <Translate contentKey="larissAsstApp.orderProduct.detail.title">OrderProduct</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{orderProductEntity.id}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="larissAsstApp.orderProduct.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{orderProductEntity.quantity}</dd>
          <dt>
            <span id="totalPrice">
              <Translate contentKey="larissAsstApp.orderProduct.totalPrice">Total Price</Translate>
            </span>
          </dt>
          <dd>{orderProductEntity.totalPrice}</dd>
          <dt>
            <Translate contentKey="larissAsstApp.orderProduct.order">Order</Translate>
          </dt>
          <dd>{orderProductEntity.order ? orderProductEntity.order.id : ''}</dd>
          <dt>
            <Translate contentKey="larissAsstApp.orderProduct.productDetails">Product Details</Translate>
          </dt>
          <dd>{orderProductEntity.productDetails ? orderProductEntity.productDetails.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/order-product" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order-product/${orderProductEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrderProductDetail;

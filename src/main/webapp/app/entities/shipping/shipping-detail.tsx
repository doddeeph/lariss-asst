import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './shipping.reducer';

export const ShippingDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const shippingEntity = useAppSelector(state => state.shipping.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="shippingDetailsHeading">
          <Translate contentKey="larissAsstApp.shipping.detail.title">Shipping</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{shippingEntity.id}</dd>
          <dt>
            <span id="streetAddress">
              <Translate contentKey="larissAsstApp.shipping.streetAddress">Street Address</Translate>
            </span>
          </dt>
          <dd>{shippingEntity.streetAddress}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="larissAsstApp.shipping.city">City</Translate>
            </span>
          </dt>
          <dd>{shippingEntity.city}</dd>
          <dt>
            <span id="state">
              <Translate contentKey="larissAsstApp.shipping.state">State</Translate>
            </span>
          </dt>
          <dd>{shippingEntity.state}</dd>
          <dt>
            <span id="postalCode">
              <Translate contentKey="larissAsstApp.shipping.postalCode">Postal Code</Translate>
            </span>
          </dt>
          <dd>{shippingEntity.postalCode}</dd>
          <dt>
            <span id="country">
              <Translate contentKey="larissAsstApp.shipping.country">Country</Translate>
            </span>
          </dt>
          <dd>{shippingEntity.country}</dd>
          <dt>
            <span id="method">
              <Translate contentKey="larissAsstApp.shipping.method">Method</Translate>
            </span>
          </dt>
          <dd>{shippingEntity.method}</dd>
          <dt>
            <span id="cost">
              <Translate contentKey="larissAsstApp.shipping.cost">Cost</Translate>
            </span>
          </dt>
          <dd>{shippingEntity.cost}</dd>
        </dl>
        <Button tag={Link} to="/shipping" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/shipping/${shippingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ShippingDetail;

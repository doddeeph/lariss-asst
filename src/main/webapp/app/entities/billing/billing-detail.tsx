import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './billing.reducer';

export const BillingDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const billingEntity = useAppSelector(state => state.billing.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="billingDetailsHeading">
          <Translate contentKey="larissAsstApp.billing.detail.title">Billing</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{billingEntity.id}</dd>
          <dt>
            <span id="streetAddress">
              <Translate contentKey="larissAsstApp.billing.streetAddress">Street Address</Translate>
            </span>
          </dt>
          <dd>{billingEntity.streetAddress}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="larissAsstApp.billing.city">City</Translate>
            </span>
          </dt>
          <dd>{billingEntity.city}</dd>
          <dt>
            <span id="state">
              <Translate contentKey="larissAsstApp.billing.state">State</Translate>
            </span>
          </dt>
          <dd>{billingEntity.state}</dd>
          <dt>
            <span id="postalCode">
              <Translate contentKey="larissAsstApp.billing.postalCode">Postal Code</Translate>
            </span>
          </dt>
          <dd>{billingEntity.postalCode}</dd>
          <dt>
            <span id="country">
              <Translate contentKey="larissAsstApp.billing.country">Country</Translate>
            </span>
          </dt>
          <dd>{billingEntity.country}</dd>
        </dl>
        <Button tag={Link} to="/billing" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/billing/${billingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BillingDetail;

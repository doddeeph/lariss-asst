import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './strap-color.reducer';

export const StrapColorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const strapColorEntity = useAppSelector(state => state.strapColor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="strapColorDetailsHeading">
          <Translate contentKey="larissAsstApp.strapColor.detail.title">StrapColor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{strapColorEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="larissAsstApp.strapColor.name">Name</Translate>
            </span>
          </dt>
          <dd>{strapColorEntity.name}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="larissAsstApp.strapColor.value">Value</Translate>
            </span>
          </dt>
          <dd>{strapColorEntity.value}</dd>
        </dl>
        <Button tag={Link} to="/strap-color" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/strap-color/${strapColorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StrapColorDetail;
